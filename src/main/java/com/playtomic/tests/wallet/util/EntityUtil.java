package com.playtomic.tests.wallet.util;

import com.playtomic.tests.wallet.exception.BusinessException;
import com.playtomic.tests.wallet.service.BusinessExceptionCodeEnum;

import java.util.Optional;

public final class EntityUtil {

    public static <E> E getOrThrowEx(Optional<E> entity) {
        if (entity.isPresent()) return entity.get();
        else throw new BusinessException(BusinessExceptionCodeEnum.ENTITY_NOT_FOUND);
    }

}
