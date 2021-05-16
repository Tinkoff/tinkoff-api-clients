

# PaymentStatusResponse


## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**meta** | **Object** | Дополнительные метаданные в формате JSON. Сохраняются на создании платежа и возвращаются при получении статуса |  [optional]
**status** | [**StatusEnum**](#StatusEnum) |  | 



## Enum: StatusEnum

Name | Value
---- | -----
IN_PROGRESS | &quot;IN_PROGRESS&quot;
EXECUTED | &quot;EXECUTED&quot;
FAILED | &quot;FAILED&quot;
CANCELLED | &quot;CANCELLED&quot;



