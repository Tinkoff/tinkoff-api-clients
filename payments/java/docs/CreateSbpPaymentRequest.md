

# CreateSbpPaymentRequest


## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**amount** | **BigDecimal** | Сумма платежа в рублях | 
**documentNumber** | **Integer** | Номер распоряжения, определяемый клиентом. Заполняется на усмотрение плательщика |  [optional]
**from** | [**SbpPayerRequisites**](SbpPayerRequisites.md) |  |  [optional]
**id** | **String** |  Идентификатор платежа. Должен быть уникален в пределах интеграции как для платежей через СБП, так и &lt;a href&#x3D;\&quot;#operation/PaymentsCorePay\&quot;&gt;платежей по банковским реквизитам&lt;/a&gt;. | 
**purpose** | **String** | Назначение платежа, обязательна информация по НДС | 
**revenueTypeCode** | [**RevenueTypeCodeEnum**](#RevenueTypeCodeEnum) | Код вида выплаты. Подробнее: http://www.consultant.ru/document/cons_doc_LAW_353568/527cf8edd2262cb7068cafd44ed596d9a05dd237/ |  [optional]
**to** | [**SbpReceiverRequisites**](SbpReceiverRequisites.md) |  | 



## Enum: RevenueTypeCodeEnum

Name | Value
---- | -----
_1 | &quot;1&quot;
_2 | &quot;2&quot;
_3 | &quot;3&quot;



