package com.farecalulator.validator;

import com.farecalulator.exception.ApplicationException;

public interface Validator<T> {
    void validate(T t) throws ApplicationException;
}
