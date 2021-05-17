# # CreateSbpPaymentRequest

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**amount** | **float** | Сумма платежа в рублях |
**document_number** | **int** | Номер распоряжения, определяемый клиентом. Заполняется на усмотрение плательщика | [optional]
**from** | [**\OpenAPI\Client\tinkoff\api\\SbpPayerRequisites**](SbpPayerRequisites.md) |  | [optional]
**id** | **string** | Идентификатор платежа. Должен быть уникален в пределах интеграции как для платежей через СБП, так и &lt;a href&#x3D;\&quot;#operation/PaymentsCorePay\&quot;&gt;платежей по банковским реквизитам&lt;/a&gt;. |
**purpose** | **string** | Назначение платежа, обязательна информация по НДС |
**revenue_type_code** | **string** | Код вида выплаты. Подробнее: http://www.consultant.ru/document/cons_doc_LAW_353568/527cf8edd2262cb7068cafd44ed596d9a05dd237/ | [optional]
**to** | [**\OpenAPI\Client\tinkoff\api\\SbpReceiverRequisites**](SbpReceiverRequisites.md) |  |

[[Back to Model list]](../../README.md#models) [[Back to API list]](../../README.md#endpoints) [[Back to README]](../../README.md)
