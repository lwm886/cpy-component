package com.cpt.enc.test;

import com.cpt.enc.reflect.Desensitized;
import com.cpt.enc.reflect.Mode;
import lombok.Data;

/**
 * @author lw
 * @since 2021/2/5
 **/
@Data
public class Detail {
    @Desensitized(mode = Mode.PHONE)
    private String phone="15610011001";
}
