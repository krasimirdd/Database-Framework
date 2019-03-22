package alararestaurant.service;

import alararestaurant.domain.dtos.EmployeeImportDTO;
import alararestaurant.domain.entities.Employee;
import alararestaurant.domain.entities.Position;
import alararestaurant.repository.EmployeeRepository;
import alararestaurant.repository.PositionRepository;
import alararestaurant.util.FileUtil;
import alararestaurant.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    public static final String EMPLOYEES_JSON_FILE_PATH = System.getProperty("user.dir") + "/src/main/resources/files/employees.json";

    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;
    private final FileUtil fileUtil;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final Gson gson;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, PositionRepository positionRepository, FileUtil fileUtil, ValidationUtil validationUtil, ModelMapper modelMapper, Gson gson) {
        this.employeeRepository = employeeRepository;
        this.positionRepository = positionRepository;
        this.fileUtil = fileUtil;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.gson = gson;
    }

    @Override
    public Boolean employeesAreImported() {
        return this.employeeRepository.count() > 0;
    }

    @Override
    public String readEmployeesJsonFile() throws IOException {
        return this.fileUtil.readFile(EMPLOYEES_JSON_FILE_PATH);
    }

    @Override
    public String importEmployees(String employees) {

        StringBuilder importResult = new StringBuilder();
        EmployeeImportDTO[] employeeImportDTOS = this.gson.fromJson(employees, EmployeeImportDTO[].class);

        Arrays.stream(employeeImportDTOS)
                .forEach(employeeImportDTO -> {

                    Position positionEntity = this.positionRepository.findByName(employeeImportDTO.getPosition()).orElse(null);

                    if (this.validationUtil.isValid(employeeImportDTO)) {

                        if (positionEntity == null) {

                            if (this.validationUtil.isValid(employeeImportDTO.getPosition())) {
                                positionEntity = new Position();
                                positionEntity.setName(employeeImportDTO.getPosition());
                                this.positionRepository.saveAndFlush(positionEntity);
                            } else {
                                return;
                            }
                        }

                        Employee employeeEntity = this.modelMapper.map(employeeImportDTO, Employee.class);
                        employeeEntity.setPosition(positionEntity);
                        this.employeeRepository.saveAndFlush(employeeEntity);

                        importResult.append(String.format("Record %s successfully imported.", employeeEntity.getName()))
                                .append(System.lineSeparator());
                    } else {

                        importResult.append("Invalid data format.").append(System.lineSeparator());
                    }
                });

        return importResult.toString().trim();
    }
}
