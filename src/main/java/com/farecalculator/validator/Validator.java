package com.farecalculator.validator;

import com.farecalculator.exception.ApplicationException;

public interface Validator<T> {
    void validate(T t) throws ApplicationException;
}
