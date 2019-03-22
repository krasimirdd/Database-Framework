package alararestaurant.service;

import alararestaurant.domain.dtos.xml_dtos.OrderImportRootDTO;
import alararestaurant.domain.entities.Employee;
import alararestaurant.domain.entities.Order;
import alararestaurant.domain.entities.OrderItem;
import alararestaurant.repository.EmployeeRepository;
import alararestaurant.repository.ItemRepository;
import alararestaurant.repository.OrderItemRepository;
import alararestaurant.repository.OrderRepository;
import alararestaurant.util.FileUtil;
import alararestaurant.util.ValidationUtil;
import alararestaurant.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    public static final String ORDERS_XML_FILE_PATH = System.getProperty("user.dir") + "/src/main/resources/files/orders.xml";

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final EmployeeRepository employeeRepository;
    private final OrderItemRepository orderItemRepository;
    private final FileUtil fileUtil;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ItemRepository itemRepository, EmployeeRepository employeeRepository, OrderItemRepository orderItemRepository, FileUtil fileUtil, XmlParser xmlParser, ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
        this.employeeRepository = employeeRepository;
        this.orderItemRepository = orderItemRepository;
        this.fileUtil = fileUtil;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }


    @Override
    public Boolean ordersAreImported() {
        return this.orderRepository.count() > 0;
    }

    @Override
    public String readOrdersXmlFile() throws IOException {
        return this.fileUtil.readFile(ORDERS_XML_FILE_PATH);
    }

    @Override
    public String importOrders() throws JAXBException {

        StringBuilder importResult = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        OrderImportRootDTO orderImportRootDTO = this.xmlParser.parseXml(OrderImportRootDTO.class, ORDERS_XML_FILE_PATH);

        Arrays.stream(orderImportRootDTO.getOrderImportDTOS())
                .forEach(orderImportDTO -> {

                    Employee employeeEntity = this.employeeRepository.findByName(orderImportDTO.getEmployee()).orElse(null);

                    if (!this.validationUtil.isValid(orderImportDTO) || employeeEntity == null) {

                        importResult.append("Invalid data format.").append(System.lineSeparator());

                        return;
                    }
                    Order orderEntity = this.modelMapper.map(orderImportDTO, Order.class);
                    orderEntity.setDateTime(LocalDate.parse(orderImportDTO.getDateTime(), formatter));
                    orderEntity.setEmployee(employeeEntity);
                    this.orderRepository.saveAndFlush(orderEntity);

                    List<OrderItem> orderItems = new ArrayList<>();
                    Arrays.stream(orderImportDTO.getItemImportRootDTO().getItemImportDTOS())
                            .forEach(itemImportDTO -> {

                                if (!this.validationUtil.isValid(itemImportDTO)) {
                                    importResult.append("Invalid data format.").append(System.lineSeparator());

                                    return;
                                }

                                OrderItem orderItemEntity = this.modelMapper.map(itemImportDTO, OrderItem.class);
                                orderItemEntity.setItem(this.itemRepository.findByName(itemImportDTO.getName()).orElse(null));
                                orderItemEntity.setQuantity(itemImportDTO.getQuantity());
                                orderItemEntity.setOrder(orderEntity);
                                this.orderItemRepository.saveAndFlush(orderItemEntity);

                                orderItems.add(orderItemEntity);
                            });


                    orderEntity.setOrderItems(orderItems);
                    this.orderRepository.saveAndFlush(orderEntity);

                    importResult.append(String.format("Order for %s on %s added", orderEntity.getCustomer(), orderEntity.getDateTime()))
                            .append(System.lineSeparator());

                });

        return importResult.toString().trim();
    }

    @Override
    public String exportOrdersFinishedByTheBurgerFlippers() {

        StringBuilder exportResult = new StringBuilder();

        List<Order> orders = this.orderRepository.exportOrders();

        orders.forEach(
                order -> {

                    exportResult.append(String.format("Name: %s", order.getEmployee().getName()))
                            .append(System.lineSeparator());
                    exportResult.append("Orders:")
                            .append(System.lineSeparator());
                    exportResult.append(String.format(" Customer: %s", order.getCustomer()))
                            .append(System.lineSeparator());
                    exportResult.append(" Items:")
                            .append(System.lineSeparator());

                    order.getOrderItems().forEach(
                            orderItem -> {

                                exportResult.append(String.format("  Name: %s", orderItem.getItem().getName()))
                                        .append(System.lineSeparator());
                                exportResult.append(String.format("  Price: %s", orderItem.getItem().getPrice()))
                                        .append(System.lineSeparator());
                                exportResult.append(String.format("  Quantity: %s", orderItem.getQuantity()))
                                        .append(System.lineSeparator());

                                exportResult.append(System.lineSeparator());
                            });

                }

        );

        return exportResult.toString().trim();
    }
}
