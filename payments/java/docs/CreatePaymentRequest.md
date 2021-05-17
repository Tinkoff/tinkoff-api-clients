

# CreatePaymentRequest


## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**amount** | **BigDecimal** | Сумма платежа в рублях | 
**collectionAmount** | **BigDecimal** | Удержанная сумма в рублях |  [optional]
**documentNumber** | **Integer** | Номер распоряжения, определяемый клиентом. Заполняется на усмотрение плательщика |  [optional]
**dueDate** | **OffsetDateTime** | Дата до которой необходимо провести платёж, при неуспешных попытках платежа он будет повторён вплоть до указанной даты. Если дату не передать, то платеж не будет повторён в случае неуспешной попытки. Время на проведение платежа не может быть больше 30 дней. |  [optional]
**executionOrder** | **Integer** | Очерёдность платежа |  [optional]
**from** | [**PayerRequisites**](PayerRequisites.md) |  | 
**id** | **String** | Идентификатор платежа. Должен быть уникален в пределах интеграции | 
**meta** | **Object** | Дополнительные метаданные в формате JSON. Сохраняются на создании платежа и возвращаются при получении статуса |  [optional]
**purpose** | **String** | Назначение платежа.  ВАЖНО: При заполнении назначения платежа для налоговых платежей за третьих лиц необходимо следовать шаблону:  ИНН того, кто перечисляет//КПП того, кто перечисляет//Наименование лица, за которого происходит оплата//назначение платежа.  Подробнее: https://glavkniga.ru/situations/s509587 | 
**revenueTypeCode** | [**RevenueTypeCodeEnum**](#RevenueTypeCodeEnum) | Код вида выплаты. Подробнее: http://www.consultant.ru/document/cons_doc_LAW_353568/527cf8edd2262cb7068cafd44ed596d9a05dd237/ |  [optional]
**tax** | [**TaxPaymentParameters**](TaxPaymentParameters.md) |  |  [optional]
**to** | [**ReceiverRequisites**](ReceiverRequisites.md) |  | 
**uin** | **String** | Уникальный идентификатор платежа. Поле платежки 22.  ВАЖНО: При налоговом платеже поле обязательно должно быть заполнено.  Подробнее: https://www.glavbukh.ru/art/98739-uin-v-platejnom-poruchenii-2021 |  [optional]



## Enum: RevenueTypeCodeEnum

Name | Value
---- | -----
_1 | &quot;1&quot;
_2 | &quot;2&quot;
_3 | &quot;3&quot;



