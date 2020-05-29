package com.vulenhtho.mrssso.util;

import com.vulenhtho.mrssso.config.Constant;
import org.springframework.data.domain.Sort;

public class CommonUtils {

    public static Sort getSort(String typeDateSort) {
        if (typeDateSort != null) {
            switch (typeDateSort) {
                case Constant.DATE_DES:
                    return Sort.by("createdDate").descending();
                case Constant.DATE_ASC:
                    return Sort.by("createdDate").ascending();
                case Constant.MODIFIED_DES:
                    return Sort.by("lastModifiedDate").descending();
            }
        }
        return Sort.by("createdDate").descending();
    }
}
