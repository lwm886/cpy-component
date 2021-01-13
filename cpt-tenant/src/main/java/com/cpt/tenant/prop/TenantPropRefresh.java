package com.cpt.tenant.prop;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author lw
 * @since 2021/1/12
 **/
@Slf4j
@Component
public class TenantPropRefresh implements ApplicationContextAware {

    @Autowired
    private TenantRegisterMap tenantRegisterMap;

    private final RefreshScope refreshScope;

    private ApplicationContext applicationContext;

    public TenantPropRefresh(final RefreshScope refreshScope) {
        this.refreshScope = refreshScope;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }

    @ApolloConfigChangeListener(value={TenantRegisterMap.NAME_SPACE})
    public void onChange(ConfigChangeEvent configChangeEvent){
        log.debug("tenant refresh before {}",tenantRegisterMap);
        refreshScope.refreshAll();
        tenantRegisterMap.refresh();
    }


}
