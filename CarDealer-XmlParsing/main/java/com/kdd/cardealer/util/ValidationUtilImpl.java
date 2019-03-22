package com.kdd.cardealer.util;

    import javax.validation.ConstraintViolation;
    import javax.validation.Validation;
    import javax.validation.Validator;
    import java.util.Set;

    public class ValidationUtilImpl implements ValidationUtil {

    private Validator validator;

    public ValidationUtilImpl() {
        this.validator =
                Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Override
    public <T> Boolean isValid(T obj) {
        return this.validator.validate(obj).size() == 0;
    }

    @Override
    public <O> Set<ConstraintViolation<O>> violations(O obj) {
        return this.validator.validate(obj);
    }
}
