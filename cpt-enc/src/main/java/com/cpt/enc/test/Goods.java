package com.cpt.enc.test;

import com.cpt.enc.core.Desensitized;
import com.cpt.enc.core.SensitiveTypeEnum;
import lombok.Data;

/**
 * @author lw
 * @since 2021/1/21
 **/
@Data
public class Goods {
    @Desensitized(SensitiveTypeEnum.MOBILE_PHONE)
    private String phone;
    @Desensitized(SensitiveTypeEnum.ID_CARD)
    private String idCard;
    private String sn;
}
