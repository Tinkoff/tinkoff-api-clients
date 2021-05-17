# # CreatePaymentRequest

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**amount** | **float** | Сумма платежа в рублях |
**collection_amount** | **float** | Удержанная сумма в рублях | [optional]
**document_number** | **int** | Номер распоряжения, определяемый клиентом. Заполняется на усмотрение плательщика | [optional]
**due_date** | [**\DateTime**](\DateTime.md) | Дата до которой необходимо провести платёж, при неуспешных попытках платежа он будет повторён вплоть до указанной даты. Если дату не передать, то платеж не будет повторён в случае неуспешной попытки. Время на проведение платежа не может быть больше 30 дней. | [optional]
**execution_order** | **int** | Очерёдность платежа | [optional]
**from** | [**\OpenAPI\Client\tinkoff\api\\PayerRequisites**](PayerRequisites.md) |  |
**id** | **string** | Идентификатор платежа. Должен быть уникален в пределах интеграции |
**meta** | **object** | Дополнительные метаданные в формате JSON. Сохраняются на создании платежа и возвращаются при получении статуса | [optional]
**purpose** | **string** | Назначение платежа.  ВАЖНО: При заполнении назначения платежа для налоговых платежей за третьих лиц необходимо следовать шаблону:  ИНН того, кто перечисляет//КПП того, кто перечисляет//Наименование лица, за которого происходит оплата//назначение платежа.  Подробнее: https://glavkniga.ru/situations/s509587 |
**revenue_type_code** | **string** | Код вида выплаты. Подробнее: http://www.consultant.ru/document/cons_doc_LAW_353568/527cf8edd2262cb7068cafd44ed596d9a05dd237/ | [optional]
**tax** | [**\OpenAPI\Client\tinkoff\api\\TaxPaymentParameters**](TaxPaymentParameters.md) |  | [optional]
**to** | [**\OpenAPI\Client\tinkoff\api\\ReceiverRequisites**](ReceiverRequisites.md) |  |
**uin** | **string** | Уникальный идентификатор платежа. Поле платежки 22.  ВАЖНО: При налоговом платеже поле обязательно должно быть заполнено.  Подробнее: https://www.glavbukh.ru/art/98739-uin-v-platejnom-poruchenii-2021 | [optional]

[[Back to Model list]](../../README.md#models) [[Back to API list]](../../README.md#endpoints) [[Back to README]](../../README.md)
