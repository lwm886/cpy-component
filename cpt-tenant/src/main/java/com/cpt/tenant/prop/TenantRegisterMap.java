package com.cpt.tenant.prop;

import com.alibaba.fastjson.JSON;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @author lw
 * @since 2021/1/11
 **/
@Slf4j
@Component
public class TenantRegisterMap {
    public static final String NAME_SPACE="custom_prop";
    private final Map<Long,Long> map=new HashMap<>();
    private final Map<String,String> apolloSpaceMap=new HashMap<>();


    public Map<Long,Long> getTenantRegisterMap(){
        return map;
    }

    public Map<String,String> getApolloSpaceMap(){
        return apolloSpaceMap;
    }

    @PostConstruct
    public void refresh(){
        Config config = ConfigService.getConfig(NAME_SPACE);
        Set<String> propertyNames = config.getPropertyNames();
        log.debug("name space custom_prop propertyNames:{}",propertyNames);
        if(CollectionUtils.isEmpty(propertyNames)){
            return;
        }
        apolloSpaceMap.clear();
        List<TenantProp> list= Lists.newArrayList();
        propertyNames.forEach(propertyName->{
            apolloSpaceMap.put(propertyName,config.getProperty(propertyName,null));
            TenantProp tenantProp = config.getProperty(propertyName, text -> JSON.parseObject(text, TenantProp.class), null);
            if(Objects.nonNull(tenantProp) && Objects.nonNull(tenantProp.getTaskId()) && Objects.nonNull(tenantProp.getTenantId())){
                list.add(tenantProp);
            }
        });
        map.clear();
        for(TenantProp tenantProp:list){
            map.put(tenantProp.getTaskId(),tenantProp.getTenantId());
        }
        log.debug("apollo space:{}",JSON.toJSONString(apolloSpaceMap));
        log.debug("TenantRegisterMap refreshed success TenantRegisterMap:{}",map);
    }
}
