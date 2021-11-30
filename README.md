## 开发组件
cpt-tenant 租户管理组件
cpt-enc 应用交互加密组件
加密机制包括：FASTJSON机制和反射机制，推荐使用反射机制。

fastjson机制使用示例：
在rest接口上标记注解 @EnableDesensitized
在要返回的实体上标记，如     @JSONField(label = "mobile",serializeUsing = SerializerUsed.class)  对手机号进行脱敏
