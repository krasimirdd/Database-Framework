package com.kdd.cardealer.util;

import javax.validation.ConstraintViolation;
import java.util.Set;

public interface ValidationUtil {

    <T> Boolean isValid(T obj);

    <O> Set<ConstraintViolation<O>> violations(O obj);
}
