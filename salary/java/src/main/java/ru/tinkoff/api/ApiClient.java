/*
 * Tinkoff Business OpenApi
 * # Введение   **Tinkoff Business OpenApi** помогает автоматизировать бизнес-процессы с помощью интеграции ваших сервисов с сервисами Тинькофф: получение выписки со счетов, выставление счета, совершение платежей и других.   Два способа подключения:   [Партнерский сценарий](#section/Partnerskij-scenarij) - чтобы подключить данные из Тинькофф Бизнеса к другим сервисам.   [Селф-Сервис](#section/Scenarij-Self-Servis) - чтобы получить доступ через API к своей компании в Тинькофф Бизнесе.  # Терминология  * **Access Token** — токен, который дает доступ до методов Tinkoff Business OpenApi. Для партнерского сценария, токен будет активен (т.е. по нему можно будет вызывать методы) 30 минут: потом его надо будет переполучить. Для сценария \"Селф-Сервис\" токен будет активен 1 год. * **Refresh Token** — токен, активный 1 год, который дает возможность получить новый Access Token, если он устарел, чтобы восстановить доступ до методов Tinkoff Business OpenApi. * **redirect_uri** — uri для перехода, который вы указываете при регистрации партнера. На него будет произведен редирект для получения Access и Refresh токенов. Подробнее про формат запросов, который должен обрабатывать эндпоинт на этом uri, можно почитать [здесь.](#section/Partnerskij-scenarij/Opisanie-processa-avtorizacii)   * **client_id** - уникальный идентификатор партнера, которые вы получаете при регистрации   * **scope** - механизм, [используемый в OAuth 2.0](https://oauth.net/2/scope/), который ограничивает права доступа вашего приложения к пользовательским данным. Выпущенный в личном кабинете токен будет ограничен выбранными в меню доступами.  # Авторизация в Tinkoff Id  **Tinkoff Id** — единая точка авторизации для всего Тинькофф банка, работающая по протоколам [**OAuth 2.0**]( https://oauth.net/2/) и **[OpenID Connect](https://openid.net/connect/)**. Авторизация для всех методов Tinkoff Business OpenApi работает путем отправки Access Token в заголовке запроса.  # Партнерский сценарий  ## Регистрация   Для начала работы с Tinkoff Business OpenApi в качестве партнера заполните заявку на подключение на [этой странице](https://www.tinkoff.ru/business/open-api/). После рассмотрения вашей заявки вы получите **client_id** и пароль.  ## Описание процесса авторизации  В процессе регистрации вас попросят указать redirect_uri. Вам понадобится создать эндпоинт, доступный по ```redirect_uri```, который заканчивает процесс авторизации, путем обмена кода на Access и Refresh токены. Для удобства, здесь и далее, он будет лежать на  ```https://myintegration.ru/auth/complete```, где ```https://myintegration.ru``` - это ваше приложение.  Описание процесса авторизации:  1. Клиент начинает процесс авторизации.  2. Вам понадобится сгенерировать параметр state и связать его с браузером пользователя для защиты от CSRF-атак.   `state` параметр служит для повышения надежности процесса авторизации. Правильное его использование повышает безопасность вашей интеграции с `Tinkoff Business OpenApi`. Почитать про лучшие практики использования `state` можно [здесь.](https://tools.ietf.org/html/draft-ietf-oauth-security-topics-14#section-4.7) 3. Вызвать метод ```GET https://id.tinkoff.ru/auth/authorize``` со следующими параметрами: * client_id: идентификатор, полученный при регистрации партнера * redirect_uri: ```https://myintegration.ru/auth/complete``` * state: строка из пункта 2 * response_type: ```code``` * scope_parameters: URL закодированный json с ИНН и КПП компании клиента. Эти данные необходимо для того, чтобы уметь однозначно определять к какой именно компании давать доступ клиенту. Если у компании клиента нет КПП, передайте вместо него \"0\". Пример JSON до кодирования:   ``` {      \"inn\" : \"7743180892\",      \"kpp\" : \"773101001\"  }  ```  Пример запроса:  ``` GET https://id.tinkoff.ru/auth/authorize?client_id=%client_id%&redirect_uri=https://myintegration.ru/auth/complete&state=ABCxyz&response_type=code&scope_parameters=%20%7B%20%22inn%22%20:%20%227743180892%22,%20%22kpp%22%20:%20%22773101001%22%20%7D ```  4. Клиенту откроется окно с выбором доступов:  ![](https://business.cdn-tinkoff.ru/static/images/opensme/consents-pop-up.jpg)   Если клиент поставит галочки напротив всех полей в списке и нажмет \"Продолжить\", при следующих авторизациях это окно открываться не будет.  После нажатия кнопки \"Продолжить\", запрос закончится редиректом на ```https://myintegration.ru/auth/complete```.  5. На ```https://myintegration.ru/auth/complete``` придет запрос вида:  ``` https://myintegration.ru/auth/complete?state=ABCxyz&code=c.1aGiAXX3Ni&session_state=hU7GNp0Y3kgs3nx0H3RTj3JzCSrdaqaDhU6lS87eiUw.i4kl6dsEB1SQogzq0Nj0 ```   Далее вам нужно провалидировать параметр state. Если все правильно, то вам надо забрать строку из параметра ```code```  для того, чтобы обменять ее на Access и Refresh токены.  6. Для того, чтобы получить Access и Refresh токены, вам нужно вызвать метод ```POST https://id.tinkoff.ru/auth/token```.  Формат запроса:  * Authorization: Basic, где username и password соответствуют client-id и паролю из пункта 2. [Примеры составления в разных языках](https://gist.github.com/brandonmwest/a2632d0a65088a20c00a) * Content-type: ```application/x-www-form-urlencoded``` * grant_type: ```authorization_code``` * redirect_uri: ```https://myintegration.ru/auth/complete``` * code: код из пункта 5.  Пример запроса: ```  curl -X POST \\      https://id.tinkoff.ru/auth/token \\      -H 'Authorization: Basic ' \\      -H 'Content-Type: application/x-www-form-urlencoded' \\      -d 'grant_type=authorization_code&redirect_uri=https://myintegration.ru/auth/complete&code=c.1aGiAXX3Ni' ``` Пример ответа: ``` {     \"access_token\": \"t.mzucgRIDwalMAQ_at2Y2kmJB6yhNAQlWMNucp3w45xMcKknxWyl_8sWkkp5_3Nq8i_UvddDroJvd3elz-QH5hQ\",     \"token_type\": \"Bearer\",     \"expires_in\": 1791, // Время жизни токена в секундах     \"refresh_token\": \"LShO9uuTkeWqozxO8WP2eGsJpLBQQ-3QBGYUeNzUv4LTtjudU6zPofXbiMwToznuCOLv65tpCJn04fsLvsYH2Q\" } ```  7. После того как вы получили токен, мы рекомендуем вам проверить, что клиент предоставил нужные доступы и что вы правильно передали данные в scope_parameters. Для того, чтобы это сделать, вам нужно вызвать метод ```POST https://id.tinkoff.ru/auth/introspect``` и проверить данные в поле scope в ответе:  Формат запроса:   * Authorization: Basic, где username и password соответствуют client-id и паролю из пункта 2. [Примеры составления в разных языках](https://gist.github.com/brandonmwest/a2632d0a65088a20c00a) * Content-type: ```application/x-www-form-urlencoded``` * token — тело токена, полученного на предыдущем шаге.  Пример запроса: ```  curl -X POST \\      https://id.tinkoff.ru/auth/introspect \\      -H 'Authorization: Basic ' \\      -H 'Content-Type: application/x-www-form-urlencoded' \\      -d 'token=t.mzucgRIDwalMAQ_at2Y2kmJB6yhNAQlWMNucp3w45xMcKknxWyl_8sWkkp5_3Nq8i_UvddDroJvd3elz-QH5hQ' ```  Пример ответа: ``` {    \"active\":true,    \"scope\":[       \"device_id\",       \"opensme/inn/[7743180892]/kpp/[773101001]/payments/draft/create\"       \"opensme\"    ],    \"client_id\":\"opensme\",    \"token_type\":\"access_token\",    \"exp\":1585728196,    \"iat\":1585684996,    \"sub\":\"2f1d967c-8596-4a1d-9bc5-b448a07e15ba\",    \"aud\":[      \"ibsme\",      \"companyInfo\"    ],    \"iss\":\"https://id.tinkoff.ru/\" } ```  В ответе вас интересует поле scope — в нем содержится список доступов, которые предоставил пользователь. В этом поле вам надо найти нужный для вас доступ. Для каждого метода в Tinkoff Business OpenApi в описании вы можете найти шаблон доступа, который должен присутствовать в поле scope, чтобы метод отработал успешно. По токену, который вы получили в предыдущем шаге, можно создавать черновики платежных поручений по компании с инн ```7743180892``` и кпп ```773101001```  (потому что есть доступ ```opensme/inn/[7743180892]/kpp/[773101001]/payments/draft/create```), но по этому токену нельзя посмотреть информацию по компании (так как нет доступа ```opensme/inn/[7743180892]/kpp/[773101001]/company-info/get```). Проверяйте и наличие доступов и правильность переданных данных.  ## SDK **SDK** - готовые реализации API Tinkoff ID для наиболее распространенных платформ. Использование SDK упрощает разработку и сокращает время на интеграцию ваших мобильных приложений.   Подробная информация выложена в соответствующие репозитории по ссылкам ниже:  * [Android SDK](https://github.com/tinkoff-mobile-tech/TinkoffID-Android)  * [iOS SDK](https://github.com/tinkoff-mobile-tech/TinkoffID-iOS)     Для ознакомления со стайлгайдами перейдите по [ссылке](https://www.figma.com/file/TsgXOeAqFEePVIosk0W7kP/Tinkoff-ID?node-id=16%3A723). # Сценарий Селф-Сервис  ## Общие сведения  Селф-сервис в Tinkoff Business OpenApi позволяет вам выпускать долгоживущий (до одного года) Access Token в личное пользование для своей компании. В данный момент, доступ до выпуска токена есть только у директоров компаний, у которых подключен продукт \"Счета и Платежи\". **Внимание: не передавайте данный токен третьим лицам, так как они могут получить доступ ко всей финансовой деятельности вашей компании.**  ## Получение токена  Что нужно для того, чтобы получить токен:  1. Откройте меню. 2. Выберите *Интеграции*:    ![](https://business.cdn-tinkoff.ru/static/images/opensme/menu.png) 3. Нажмите на кнопку *Подключить* в блоке \"Интеграция с Тинькофф по API\":    ![](https://business.cdn-tinkoff.ru/static/images/opensme/integrations.png) 4. Откроется следующий экран:    ![](https://business.cdn-tinkoff.ru/static/images/opensme/token-ip-scope-form.png) 5. Введите IP адреса, с которых вы будете делать запросы в Tinkoff Business OpenApi. Для того чтобы ввести более одного ip адреса,введите их через запятую.  6. В выпадающем списке выберите, к чему можно будет получить доступ по выпущенному токену:    ![](https://business.cdn-tinkoff.ru/static/images/opensme/token-scope-form.png) 7. Нажмите на кнопку \"Выпустить токен\". 8. На экране отобразится выпущенный токен:    ![](https://business.cdn-tinkoff.ru/static/images/opensme/token-revealed.jpg) 9. В этом окне вы сможете скопировать свой Access Token. **Учтите, что этот токен будет работать в контексте той компании, которая была выбрана в разделе \"Интеграции\" на момент выпуска токена**.  ## Отзыв токена  Если вы хотите деактивировать токен, вам понадобится:  1. Выбрать в шапке пункт [\"Личный Кабинет\"](https://business.tinkoff.ru/account). Откроется такая панель:    ![](https://business.cdn-tinkoff.ru/static/images/opensme/control-panel.png) 2. В ней нажать на [\"Настройки профиля\"](https://business.tinkoff.ru/account/settings/security/login) 3. Прокрутить до блока с названием \"Приложения, у которых есть доступ к аккаунту\".      ![](https://business.cdn-tinkoff.ru/static/images/opensme/Token-tab.png) 4. Выпущенные для селф-сервиса токены будут иметь название \"Tinkoff Business OpenApi\". Открыв вкладку с таким токеном, его можно отозвать нажав на кнопку \"Удалить доступ\". # Работа с полученными токенами  ## Работа с Access токеном  Для того, чтобы использовать Access Token, добавьте заголовок Authorization со значением ```Bearer %token%```.  Пример использования: ``` curl -X GET \\   https://business.tinkoff.ru/openapi/api/v1/company \\   -H 'Authorization: Bearer t.L4c14_rCVNbXueBPC2dFJ9fNqk9BnQuRGGz2fztHpwlFGLYxWNfTI0u_CZPEd0dMWCt0kA9P6TUgToC2_BRT7g' \\   -H 'Content-Type: application/json' \\ ```   ## Работа с Refresh токеном  Для того, чтобы, имея Refresh Token, получить новые Access и Refresh токены, нужно вызвать метод ```POST https://id.tinkoff.ru/auth/token```.  Формат запроса:   * Authorization: Basic, где username и password соответствуют client-id, которые были получены после регистрации.  [Примеры составления в разных языках](https://gist.github.com/brandonmwest/a2632d0a65088a20c00a) * Content-type: ```application/x-www-form-urlencoded``` * grant_type: ```refresh_token``` * refresh_token: в это поле передать ваш токен.  Пример запроса: ``` curl -X POST \\   https://id.tinkoff.ru/auth/token \\   -H 'Content-Type: application/x-www-form-urlencoded' \\   -d 'grant_type=refresh_token&refresh_token=LShO9uuTkeWqozxO8WP2eGsJpLBQQ-3QBGYUeNzUv4LTtjudU6zPofXbiMwToznuCOLv65tpCJn04fsLvsYH2Q' ```  Пример ответа: ``` {     \"access_token\": \"t.mzucgRIDwalMAQ_at2Y2kmJB6yhNAQlWMNucp3w45xMcKknxWyl_8sWkkp5_3Nq8i_UvddDroJvd3elz-QH5hQ\",     \"token_type\": \"Bearer\",     \"expires_in\": 1791, //Время жизни токена в секундах     \"refresh_token\": \"WvcsjowaPtv1t8r4KDeTyRk89NsSK0lTczBt8CqUSHyx3Xbh7SaWAsGhNIBEHBwqng8l2UZtBFeJCQL0GQrfoG\" } ```  **Внимание**  Убедитесь, что ваши обращения выполняются с актуальным Refresh token.  Если при обращении с Refresh token получена ошибка ```invalid_grant```, то срок действия токена истек, или он был отозван пользователям.  После получения ошибки ```invalid_grant``` важно не производить дальнейшие вызовы обмена токена, так как данный токен уже инвалидирован.  Для дальнейшей работы пользователю нужно повторно пройти авторизацию.  # Сертификаты В качестве дополнительной защиты на методах, расположенных на  [secured-openapi.business.tinkoff.ru](https://secured-openapi.business.tinkoff.ru),  используется [Mutual TLS](https://en.wikipedia.org/wiki/Mutual_authentication). Эти методы имеют в описании символ 🔒. Для использования Mutual TLS потребуется выпустить сертификат от Тинькофф. Срок действия сертификата - **1 год**.   ## Выпуск сертификата * Для выпуска сертификата потребуется сгенерировать приватный ключ  и [CSR](https://en.wikipedia.org/wiki/Certificate_signing_request). Для этого нужно:   * На компьютере иметь установленный [openssl](https://www.openssl.org/)   * Скачать [конфигурацию для генерации ключа](/openapi/downloads/config.cfg)   * Запустить в консоли `openssl req -new -config config.cfg -newkey rsa:2048 -nodes -keyout private.key -out request.csr`   и ввести ответы на соответствующие вопросы ```bash localhost:~$ openssl req -new -config config.cfg -newkey rsa:2048 -nodes -keyout private.key -out request.csr Generating a 2048 bit RSA private key .. .. writing new private key to 'private.key' You are about to be asked to enter information that will be incorporated into your certificate request. What you are about to enter is what is called a Distinguished Name or a DN. There are quite a few fields but you can leave some blank For some fields there will be a default value, If you enter '.', the field will be left blank. Country Name (RU) []:RU State or Province - субъект федерации, где зарегистрирована организация (например, MOSKVA  или KRASNOYARSKIJ KRAJ) []:KRASNOYARSKIJ KRAJ Locality Name - город, населенный пункт или муниципальное образование (например, MOSKVA или KRASNOYARSK) []:MOSKVA Organization Name - cокращенное наименование организации в соответствии с Выпиской из ЕГРЮЛ []:IP IVANOV IVAN IVANOVICH Organization Unit Name - наименование отдела (может отсутствовать) []: Сокращенное наименование организации в соответствии с Выпиской из ЕГРЮЛ (ФИО для ИП) []:IVANOV IVAN IVANOVICH Email - адрес электронной почты уполномоченного лица []:ivanov@email.com   Please enter the following 'extra' attributes to be sent with your certificate request A challenge password []: ```      <details>       <summary markdown=\"span\">         Для ввода ответов на вопросы используйте следующие правила транслитерации:       </summary>      <sup>    |А|Б|В|Г|Д|Е|Е|Ж|З|И|Й|К|Л|М|Н|О| |---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---| |A|B|V|G|D|E|E|ZH|Z|I|I|K|L|M|N|O|     |П|Р|С|Т|У|Ф|Х|Ц|Ч|Ш|Щ|Ы|Ъ|Э|Ю|Я| |---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---| |P|R|S|T|U|F|KH|TS|CH|SH|SHCH|Y|IE|E|IU|IA|        </sup>     </details>   ---    * В папке, в которой запускали генерацию, появятся файлы `request.csr` и    `private.key`. `private.key`<a name=\"secret\"></a> - секрет, с помощью которого будут подписываться запросы.   `request.csr` потребуется в заявлении на выпуск сертификата (далее) * Далее требуется заполнить [заявку на выпуск сертификата](/openapi/downloads/Заявка%20на%20SSL%20сертификат.docx)   * Заполнение должно соответствовать выпущенному `CSR`, сгенерированному шагом выше   * В поле `CSR-запрос на сертификат` вставить содержимое файла `request.csr` (можно открыть обычным блокнотом)   * Заявка должна уместиться на одну страницу * Cкан распечатанной и подписанной заявки, Word-файл с Заявкой, а также CSR-запрос (`request.csr`) нужно отправить  письмом на электронную почту openapi@tinkoff.ru. Письмо должно прийти с email директора компании, который был указан при регистрации в Тинькофф Бизнес.  В течение двух рабочих дней ответным письмом придет файл сертификата <a name=\"certificate\"></a>.  В случае ошибок в заявке или CSR-запросе ответным письмом придет письмо с описанием ошибок.  ## Использование сертификата  Полученный сертификат [`open-api-cert.pem`](#certificate) дает возможность использовать методы защищенные  [Mutual TLS](https://en.wikipedia.org/wiki/Mutual_authentication), которые расположены на [secured-openapi.business.tinkoff.ru](https://secured-openapi.business.tinkoff.ru). Чтобы сделать такой запрос Вам понадобится, помимо сертификата, сгенерированный ранее [секрет](#secret).  Сделать запрос можно разными способами. Например: * Через curl. Для этого используя [полученный файл](#certificate) и [секрет](#secret) выполните запрос:   `curl --cert open-api-cert.pem --key private.key https://secured-openapi.business.tinkoff.ru/...` * Через HTTP-клиент. Чтобы сделать такой запрос воспользуйтесь документацией используемого клиента.  Ниже приведено несколько примеров:   * Java - [Async Http Client](https://asynchttpclient.github.io/async-http-client/ssl.html)   * PHP - Guzzle 6 ([cert](http://docs.guzzlephp.org/en/stable/request-options.html#cert) и    [ssl-key](http://docs.guzzlephp.org/en/stable/request-options.html#ssl-key))   * Python - [Requests](https://requests.readthedocs.io/en/master/user/advanced/#client-side-certificates)   * JavaScript - пример на [Axios](https://github.com/axios/axios):   ```js   const fs = require('fs');   const https = require('https');   const axios = require('axios');   // ...   const httpsAgent = new https.Agent({       cert: fs.readFileSync('open-api-cert.pem'),       key: fs.readFileSync('private.key')   });   // ...   const result = await axios.get('https://secured-openapi.business.tinkoff.ru/...', { httpsAgent });   ``` # Трейсинг в Tinkoff Business OpenApi  В ответе каждого запроса к Tinkoff Business OpenApi будет лежать заголовок ```X-Request-Id``` — уникальный идентификатор запроса. Для диагностики работы вашего приложения, этот идентификатор с вашей стороны стоит сохранять или логировать. # Песочница  [Песочница](/openapi/sandbox/docs) предоставляет возможность воспользоваться методами openApi в ознакомительном режиме.  Действия с песочницей не воздействуют на реальные данные. В данной версии песочницы вам не нужно получать токен.  Функциональность песочницы находится в доработке, поэтому возможны достаточно частые изменения ее домена и префиксов путей. # Обратная связь  Если во время работы с Tinkoff Business OpenApi у вас возникли  вопросы, смело задавайте их путем отправки письма на openapi@tinkoff.ru. Если вы хотите предложить улучшения, создавайте issue в нашем [гитхаб репозитории](https://github.com/TinkoffCreditSystems/business-openapi). Информация по релизам будет в [телеграм канале](https://t.me/TinkoffBusinessOpenApi). # Release Notes  ## 26 апреля 2021 ### Компании #### GET /api/v1/company * [Смена юридического адреса Тинькофф Банка](https://www.tinkoff.ru/about/news/19032021-tinkoff-notifies-change-legal-address/).  ## 22 апреля 2021 ### Платежи #### POST /api/v1/payment/ruble-transfer/pay * Метод создания платежа поддерживает налоговые платежи и налоговые платежи за третьих лиц.  ## 5 апреля 2021 ### Бизнес-карты * Добавлены [методы для перевыпуска виртуальных карт](#operation/postApiV1CardVirtualReissue):   * POST /api/v1/card/virtual/reissue   * GET  /api/v1/card/virtual/reissue/result  ## 2 апреля 2021 ### Счета и выписки #### GET /api/v1/bank-statement * В ответ метода добавлено новое поле `operationId`, которое содержит уникальный идентификатор операции  ### OpenApi * Добавлена [песочница](https://business.tinkoff.ru/openapi/sandbox/docs)  ## 25 марта 2021 ### Сценарий Селф-Сервис * Перенесли возможность выпуска токена для сценария селф-сервис в раздел \"Интеграции\".  (См. [\"Получение токена\"](#section/Scenarij-Self-Servis/Poluchenie-tokena).)  ## 18 марта 2021 ### OpenApi * Добавлена спецификация ошибки при большой нагрузке на сервис  ### Зарплатный проект * Добавлены [методы для реестровых выплат сотрудникам](#tag/Zarplatnyj-proekt):   * POST /api/v1/salary/payment-registry/submit   * GET  /api/v1/salary/payment-registry/submit/result    ## 15 марта 2021 ### Счета и выписки #### GET /api/v3/bank-accounts * Добавлен новый метод для получения расчетных счетов. Отличия от старого метода:   * Добавлены спец.счета и счета бизнес-копилки #### GET /api/v2/bank-accounts * Метод теперь считается устаревшим  ## 25 февраля 2021 ### SDK * Добавлены SDK для партнерской авторизации   ## 19 февраля 2021 ### Самозанятые #### POST /api/v1/self-employed/payment-registry/create * Добавлен флаг для автоматического удержания налога `taxHolding` #### POST /api/v1/self-employed/payment-registry/create * Добавлен выбор способа создания платежного реестра `registryCreateType`  ### Зарплатный проект #### POST /api/v1/salary/payment-registry/create * Добавлен выбор способа создания платежного реестра `registryCreateType`  ### Платежи #### POST /api/v1/salary/payment-registry/create * В запрос добавилось опциональное строковое поле `revenueTypeCode`. Оно может принимать значения \"1\", \"2\" или \"3\". Подробнее смотри [тут](https://www.audit-it.ru/news/account/1013406.html). * В запрос добавилось опциональное числовое поле `collectionAmount` - yдержанная сумма в рублях. Актуально только при платежах в пользу физлиц.  ## 8 февраля 2021 ### Платежи #### POST /api/v1/payment/create * Изменён пример налогового платежа  ## 28 января 2021 ### Платежи #### POST /api/v1/payment/sbp/pay * Отчество получателя `to.middleName`, содержащееся в запросе, стало опциональным.  ## 21 января 2021 ### Платежи #### POST /api/v1/payment/create * В тело запроса добавлено необязательное поле: корреспондентский счет получателя `recipientCorrAccountNumber` (В связи с [нововведением от 2021 года](https://roskazna.gov.ru/dokumenty/sistema-kaznacheyskikh-platezhey/kaznacheyskie-scheta/), данное поле будет обязательным для налоговых платежей)  ### Счета и выписки #### GET /api/v2/bank-accounts * Исправлены неточности в документации  ## 18 января 2021 ### Торговый эквайринг #### GET /api/v1/tacq/operations/terminal/{terminalKey} * Добавлен [метод для получения операций Торгового эквайринга по терминалу](#tag/Torgovyj-ekvajring)   * GET /api/v1/tacq/operations/terminal/{terminalKey}  ### Платежи #### POST /api/v1/payment/sbp/pay *  Добавлен [метод для совершения выплаты с рублевых счетов компании на счета физлиц через систему быстрых платежей](#tag/Platezhi):     * POST /api/v1/payment/sbp/pay  ## 12 января 2021 ### Самозанятые #### GET /api/v1/self-employed/payment-registry/receipts/result * Добавлен идентификатор самозанятого в `SelfEmployedReceipt.selfEmployedInfo`  ### Спецификация OpenApi * Методы, закрытые [MTLS сертификатом](#section/Sertifikaty), имеют в описании символ 🔒  ### Счета и выписки #### GET /api/v1/bank-statement * Исправлено описание и добавлена валидация на поле `operationType`  ## 10 декабря 2020 ### Информация о пользователе * Добавлен [метод для получения информации об активных дебетовых счетах клиента](#tag/Informaciya-o-polzovatele)   * GET /api/v1/individual/accounts/debit   ## 8 декабря 2020 ### Самозанятые #### GET /api/v1/self-employed/payment-registry/ * Добавлены айди самозанятого в `selfEmployedInfo`  ### Зарплатный проект #### GET /api/v1/salary/payment-registry/ * Добавлены айди сотрудника в `employeeInfo`  ### Счета и выписки #### GET /api/v2/bank-accounts * Добавлен новый метод для получения расчетных счетов. Отличия от старого метода:   * Информация о транзитном счете возвращается вместе с основным счетом   * Возвращается информация о наименовании и типе счета, бике банка получателя  ## 3 декабря 2020 ### Партнерская доставка * Добавлен [метод, позволяющий загрузить документ в задание на партнерскую доставку](#tag/Partnerskaya-dostavka)   * POST /api/v1/delivery/documents * Добавлен [метод, позволяющий выгрузить документ по его идентификатору](#tag/Partnerskaya-dostavka)   * GET /api/v1/delivery/documents/{id}  ### Информация о пользователе Добавлен [метод для получения информации о статусе самозанятого](#tag/Informaciya-o-polzovatele): * GET /api/v1/individual/self-employed/status  ## 26 ноября 2020 ### Спецификация OpenApi * Скорректирована спецификация ошибки 400: ошибка отдается в виде json * Исправлен формат вывода ошибок перечисления  ## 16 ноября 2020 ### Бизнес-карты  #### GET /api/v1/card * Метод теперь доступен без сертификата mTLS * Убрали поле expiryDate из ответа метода  #### GET /api/v1/card/{ucid}  * Метод теперь доступен без сертификата mTLS * Убрали поле expiryDate из ответа метода  ### Спецификация OpenApi * Обновлена спецификация ошибки 403: добавлена информация о причинах ошибки  ## 9 ноября 2020 ### Выставление счета #### POST api/v1/invoice/send * В запрос добавлено опциональное поле `accountNumber`. Теперь отправитель счета может сам выбирать счет на который  он хочет получить оплату. * Поля плательщика `payer.inn`, `payer.name` стали опциональными, как и само поле `payer`, чтобы  пользователь мог выставлять счета физическим лицам не указывая лишних данных, если в них нет необходимости. * Добавлено новое опциональное поле `kpp` для плательщика. Пользователи по желанию смогут передать дополнительную  информацию о плательщике в счет.  ### Информация о пользователе * Добавлено описание [метода получения учётных данных](#section/Poluchenie-uchyotnyh-dannyh)  ### Партнерская доставка * Все методы [партнерской доставки](#tag/Partnerskaya-dostavka) закрыты mTLS  ### Бизнес-карты #### GET /api/v1/card * Добавлен новый метод для получения данных о картах компании * В запросе есть опциональный строковый параметр `agreementNumber`. Он передается, если нужен список с данными карт компании, привязанных к заданному расчетному счету. * В запросе есть опциональный числовой параметр `offset` - количество карт, которые необходимо пропустить. По умолчанию offset = 0. * В запросе есть опциональный числовой параметр `limit` - количество карт, которые необходимо вывести. По умолчанию limit = 1000. Значение для этого параметра не может быть больше 1000. #### GET /api/v1/card/{ucid} * Добавлен метод для получения данных об одной карте по UCID  ## 2 ноября 2020 ### Спецификация OpenApi #### GET /openapi/docs/openapi.yaml * Скорректирован формат загружаемого файла спецификации с .json на .yaml  ### Бизнес-карты * Изменено название раздела \"Корпоративные карты\" на \"Бизнес-карты\"  ### Платежи #### POST /api/v1/payment/create * Добавлена валидация номера документа при создании платежей. Строковое поле должно содержать не более 6 цифр.  ### Информация о пользователе Добавлены [методы для получения информации о пользователе](#tag/Informaciya-o-polzovatele):   * GET /api/v1/individual/documents/passport   * GET /api/v1/individual/documents/driver-licenses   * GET /api/v1/individual/documents/inn   * GET /api/v1/individual/documents/snils   * GET /api/v1/individual/addresses   * GET /api/v1/individual/identification/status       ### Партнерская доставка Добавлены [методы, предоставлющие функционал доставки для партнеров](#tag/Partnerskaya-dostavka)   * GET   /api/v1/delivery/tasks/{id}   * PUT   /api/v1/delivery/tasks/{id}   * POST  /api/v1/delivery/tasks   * POST  /api/v1/delivery/tasks/{id}/cancel   * POST  /api/v1/delivery/meetings   * POST  /api/v1/delivery/intervals  ### Сертификаты * Указано правило транслитерации  ## 13 октября 2020 ### Счета и выписки #### GET /api/v1/bank-statement * Поправлена ошибка, возникающая у некоторых клиентов при запросе больших выписок.   ## 5 октября 2020 ### Платежи #### POST /api/v1/payment/create * Поле `inn`, содержащееся в запросе, теперь может иметь значение \"0\". Такое допустимо, если платеж выполняется в пользу физического лица и  его ИНН неизвестен. * В документации обновлены описания полей `inn` и `kpp`, содержащиеся в запросах.  ## 24 сентября 2020 ### Платежи #### POST /api/v1/payment/ruble-transfer/pay * В документацию добавлено описание лимитов, установленных по умолчанию, на выполнение платежей:     * максимальная сумма одного платежа - 100000 рублей     * максимальная сумма платеже в день - 100000 рублей     * максимальная сумма платежей в месяц - 1000000 рублей     * максимальное количество платежей в день на одного контрагента - 3      ### Селф-сервис * Добавлена возможность выпускать токены с определенными скопами. Для подробностей смотри: Сценарий Селф-Сервис, Получение токена, n.6  ### Самозанятые Добавлены [методы для реестровых выплат самозанятым](#tag/Samozanyatye):   * POST /api/v1/self-employed/recipients/create   * GET /api/v1/self-employed/recipients/create/result   * POST /api/v1/self-employed/recipients/list   * POST /api/v1/self-employed/payment-registry/create   * GET /api/v1/self-employed/payment-registry/create/result   * GET /api/v1/self-employed/payment-registry/{paymentRegistryId}   * POST /api/v1/self-employed/payment-registry/submit   * GET /api/v1/self-employed/payment-registry/submit/result   * POST /api/v1/self-employed/payment-registry/fiscalize   * GET /api/v1/self-employed/payment-registry/fiscalize/result   * POST /api/v1/self-employed/payment-registry/receipts   * GET /api/v1/self-employed/payment-registry/receipts/result  ## 14 сентября 2020  ### Счета и выписки #### GET /api/v1/bank-statement * Поле `recipientInn`, содержащееся в ответе, стало опциональным. Оно может отсутствовать, например, в случае, если идет расчет с контрагентами, так как данное поле не является обязательным для совершения перевода. * Поле `payerAccount`, содержащееся в ответе, стало опциональным. Оно может отсутствовать, например, в случае, если, плательщиком является кредитная организация или филиал кредитной организации.  ### Платежи #### POST /api/v1/payment/create * В запрос добавилось опциональное строковое поле `revenueTypeCode`. Оно может принимать значения \"1\", \"2\" или \"3\". Подробнее смотри [тут](https://www.audit-it.ru/news/account/1013406.html). * В запрос добавилось опциональное числовое поле `collectionAmount` - yдержанная сумма в рублях. Актуально только при платежах в пользу физлиц.  ### Cертификаты * Выпущенные с данного момента сертификаты предоставляются в формате `pem`, что избавляет их владельцев от необходимости конвертации сертификатов перед использованием.
 *
 * The version of the OpenAPI document: 1.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package ru.tinkoff.api;

import okhttp3.*;
import okhttp3.internal.http.HttpMethod;
import okhttp3.internal.tls.OkHostnameVerifier;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import okio.BufferedSink;
import okio.Okio;

import javax.net.ssl.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.tinkoff.api.auth.Authentication;
import ru.tinkoff.api.auth.HttpBasicAuth;
import ru.tinkoff.api.auth.HttpBearerAuth;
import ru.tinkoff.api.auth.ApiKeyAuth;

public class ApiClient {

    private String basePath = "https://secured-openapi.business.tinkoff.ru";
    private boolean debugging = false;
    private Map<String, String> defaultHeaderMap = new HashMap<String, String>();
    private Map<String, String> defaultCookieMap = new HashMap<String, String>();
    private String tempFolderPath = null;

    private Map<String, Authentication> authentications;

    private DateFormat dateFormat;
    private DateFormat datetimeFormat;
    private boolean lenientDatetimeFormat;
    private int dateLength;

    private InputStream sslCaCert;
    private boolean verifyingSsl;
    private KeyManager[] keyManagers;

    private OkHttpClient httpClient;
    private JSON json;

    private HttpLoggingInterceptor loggingInterceptor;

    /*
     * Basic constructor for ApiClient
     */
    public ApiClient() {
        init();
        initHttpClient();

        // Setup authentications (key: authentication name, value: authentication).
        authentications.put("httpAuth", new HttpBearerAuth("bearer"));
        // Prevent the authentications from being modified.
        authentications = Collections.unmodifiableMap(authentications);
    }

    /*
     * Basic constructor with custom OkHttpClient
     */
    public ApiClient(OkHttpClient client) {
        init();

        httpClient = client;

        // Setup authentications (key: authentication name, value: authentication).
        authentications.put("httpAuth", new HttpBearerAuth("bearer"));
        // Prevent the authentications from being modified.
        authentications = Collections.unmodifiableMap(authentications);
    }

    private void initHttpClient() {
        initHttpClient(Collections.<Interceptor>emptyList());
    }

    private void initHttpClient(List<Interceptor> interceptors) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addNetworkInterceptor(getProgressInterceptor());
        for (Interceptor interceptor: interceptors) {
            builder.addInterceptor(interceptor);
        }

        httpClient = builder.build();
    }

    private void init() {
        verifyingSsl = true;

        json = new JSON();

        // Set default User-Agent.
        setUserAgent("OpenAPI-Generator/1.0.0/java");

        authentications = new HashMap<String, Authentication>();
    }

    /**
     * Get base path
     *
     * @return Base path
     */
    public String getBasePath() {
        return basePath;
    }

    /**
     * Set base path
     *
     * @param basePath Base path of the URL (e.g https://secured-openapi.business.tinkoff.ru
     * @return An instance of OkHttpClient
     */
    public ApiClient setBasePath(String basePath) {
        this.basePath = basePath;
        return this;
    }

    /**
     * Get HTTP client
     *
     * @return An instance of OkHttpClient
     */
    public OkHttpClient getHttpClient() {
        return httpClient;
    }

    /**
     * Set HTTP client, which must never be null.
     *
     * @param newHttpClient An instance of OkHttpClient
     * @return Api Client
     * @throws NullPointerException when newHttpClient is null
     */
    public ApiClient setHttpClient(OkHttpClient newHttpClient) {
        this.httpClient = Objects.requireNonNull(newHttpClient, "HttpClient must not be null!");
        return this;
    }

    /**
     * Get JSON
     *
     * @return JSON object
     */
    public JSON getJSON() {
        return json;
    }

    /**
     * Set JSON
     *
     * @param json JSON object
     * @return Api client
     */
    public ApiClient setJSON(JSON json) {
        this.json = json;
        return this;
    }

    /**
     * True if isVerifyingSsl flag is on
     *
     * @return True if isVerifySsl flag is on
     */
    public boolean isVerifyingSsl() {
        return verifyingSsl;
    }

    /**
     * Configure whether to verify certificate and hostname when making https requests.
     * Default to true.
     * NOTE: Do NOT set to false in production code, otherwise you would face multiple types of cryptographic attacks.
     *
     * @param verifyingSsl True to verify TLS/SSL connection
     * @return ApiClient
     */
    public ApiClient setVerifyingSsl(boolean verifyingSsl) {
        this.verifyingSsl = verifyingSsl;
        applySslSettings();
        return this;
    }

    /**
     * Get SSL CA cert.
     *
     * @return Input stream to the SSL CA cert
     */
    public InputStream getSslCaCert() {
        return sslCaCert;
    }

    /**
     * Configure the CA certificate to be trusted when making https requests.
     * Use null to reset to default.
     *
     * @param sslCaCert input stream for SSL CA cert
     * @return ApiClient
     */
    public ApiClient setSslCaCert(InputStream sslCaCert) {
        this.sslCaCert = sslCaCert;
        applySslSettings();
        return this;
    }

    public KeyManager[] getKeyManagers() {
        return keyManagers;
    }

    /**
     * Configure client keys to use for authorization in an SSL session.
     * Use null to reset to default.
     *
     * @param managers The KeyManagers to use
     * @return ApiClient
     */
    public ApiClient setKeyManagers(KeyManager[] managers) {
        this.keyManagers = managers;
        applySslSettings();
        return this;
    }

    public DateFormat getDateFormat() {
        return dateFormat;
    }

    public ApiClient setDateFormat(DateFormat dateFormat) {
        this.json.setDateFormat(dateFormat);
        return this;
    }

    public ApiClient setSqlDateFormat(DateFormat dateFormat) {
        this.json.setSqlDateFormat(dateFormat);
        return this;
    }

    public ApiClient setOffsetDateTimeFormat(DateTimeFormatter dateFormat) {
        this.json.setOffsetDateTimeFormat(dateFormat);
        return this;
    }

    public ApiClient setLocalDateFormat(DateTimeFormatter dateFormat) {
        this.json.setLocalDateFormat(dateFormat);
        return this;
    }

    public ApiClient setLenientOnJson(boolean lenientOnJson) {
        this.json.setLenientOnJson(lenientOnJson);
        return this;
    }

    /**
     * Get authentications (key: authentication name, value: authentication).
     *
     * @return Map of authentication objects
     */
    public Map<String, Authentication> getAuthentications() {
        return authentications;
    }

    /**
     * Get authentication for the given name.
     *
     * @param authName The authentication name
     * @return The authentication, null if not found
     */
    public Authentication getAuthentication(String authName) {
        return authentications.get(authName);
    }

        /**
        * Helper method to set access token for the first Bearer authentication.
        * @param bearerToken Bearer token
        */
    public void setBearerToken(String bearerToken) {
        for (Authentication auth : authentications.values()) {
            if (auth instanceof HttpBearerAuth) {
                ((HttpBearerAuth) auth).setBearerToken(bearerToken);
                return;
            }
        }
        throw new RuntimeException("No Bearer authentication configured!");
    }

    /**
     * Helper method to set username for the first HTTP basic authentication.
     *
     * @param username Username
     */
    public void setUsername(String username) {
        for (Authentication auth : authentications.values()) {
            if (auth instanceof HttpBasicAuth) {
                ((HttpBasicAuth) auth).setUsername(username);
                return;
            }
        }
        throw new RuntimeException("No HTTP basic authentication configured!");
    }

    /**
     * Helper method to set password for the first HTTP basic authentication.
     *
     * @param password Password
     */
    public void setPassword(String password) {
        for (Authentication auth : authentications.values()) {
            if (auth instanceof HttpBasicAuth) {
                ((HttpBasicAuth) auth).setPassword(password);
                return;
            }
        }
        throw new RuntimeException("No HTTP basic authentication configured!");
    }

    /**
     * Helper method to set API key value for the first API key authentication.
     *
     * @param apiKey API key
     */
    public void setApiKey(String apiKey) {
        for (Authentication auth : authentications.values()) {
            if (auth instanceof ApiKeyAuth) {
                ((ApiKeyAuth) auth).setApiKey(apiKey);
                return;
            }
        }
        throw new RuntimeException("No API key authentication configured!");
    }

    /**
     * Helper method to set API key prefix for the first API key authentication.
     *
     * @param apiKeyPrefix API key prefix
     */
    public void setApiKeyPrefix(String apiKeyPrefix) {
        for (Authentication auth : authentications.values()) {
            if (auth instanceof ApiKeyAuth) {
                ((ApiKeyAuth) auth).setApiKeyPrefix(apiKeyPrefix);
                return;
            }
        }
        throw new RuntimeException("No API key authentication configured!");
    }

    /**
     * Helper method to set access token for the first OAuth2 authentication.
     *
     * @param accessToken Access token
     */
    public void setAccessToken(String accessToken) {
        throw new RuntimeException("No OAuth2 authentication configured!");
    }

    /**
     * Set the User-Agent header's value (by adding to the default header map).
     *
     * @param userAgent HTTP request's user agent
     * @return ApiClient
     */
    public ApiClient setUserAgent(String userAgent) {
        addDefaultHeader("User-Agent", userAgent);
        return this;
    }

    /**
     * Add a default header.
     *
     * @param key The header's key
     * @param value The header's value
     * @return ApiClient
     */
    public ApiClient addDefaultHeader(String key, String value) {
        defaultHeaderMap.put(key, value);
        return this;
    }

    /**
     * Add a default cookie.
     *
     * @param key The cookie's key
     * @param value The cookie's value
     * @return ApiClient
     */
    public ApiClient addDefaultCookie(String key, String value) {
        defaultCookieMap.put(key, value);
        return this;
    }

    /**
     * Check that whether debugging is enabled for this API client.
     *
     * @return True if debugging is enabled, false otherwise.
     */
    public boolean isDebugging() {
        return debugging;
    }

    /**
     * Enable/disable debugging for this API client.
     *
     * @param debugging To enable (true) or disable (false) debugging
     * @return ApiClient
     */
    public ApiClient setDebugging(boolean debugging) {
        if (debugging != this.debugging) {
            if (debugging) {
                loggingInterceptor = new HttpLoggingInterceptor();
                loggingInterceptor.setLevel(Level.BODY);
                httpClient = httpClient.newBuilder().addInterceptor(loggingInterceptor).build();
            } else {
                final OkHttpClient.Builder builder = httpClient.newBuilder();
                builder.interceptors().remove(loggingInterceptor);
                httpClient = builder.build();
                loggingInterceptor = null;
            }
        }
        this.debugging = debugging;
        return this;
    }

    /**
     * The path of temporary folder used to store downloaded files from endpoints
     * with file response. The default value is <code>null</code>, i.e. using
     * the system's default tempopary folder.
     *
     * @see <a href="https://docs.oracle.com/javase/7/docs/api/java/nio/file/Files.html#createTempFile(java.lang.String,%20java.lang.String,%20java.nio.file.attribute.FileAttribute...)">createTempFile</a>
     * @return Temporary folder path
     */
    public String getTempFolderPath() {
        return tempFolderPath;
    }

    /**
     * Set the temporary folder path (for downloading files)
     *
     * @param tempFolderPath Temporary folder path
     * @return ApiClient
     */
    public ApiClient setTempFolderPath(String tempFolderPath) {
        this.tempFolderPath = tempFolderPath;
        return this;
    }

    /**
     * Get connection timeout (in milliseconds).
     *
     * @return Timeout in milliseconds
     */
    public int getConnectTimeout() {
        return httpClient.connectTimeoutMillis();
    }

    /**
     * Sets the connect timeout (in milliseconds).
     * A value of 0 means no timeout, otherwise values must be between 1 and
     * {@link Integer#MAX_VALUE}.
     *
     * @param connectionTimeout connection timeout in milliseconds
     * @return Api client
     */
    public ApiClient setConnectTimeout(int connectionTimeout) {
        httpClient = httpClient.newBuilder().connectTimeout(connectionTimeout, TimeUnit.MILLISECONDS).build();
        return this;
    }

    /**
     * Get read timeout (in milliseconds).
     *
     * @return Timeout in milliseconds
     */
    public int getReadTimeout() {
        return httpClient.readTimeoutMillis();
    }

    /**
     * Sets the read timeout (in milliseconds).
     * A value of 0 means no timeout, otherwise values must be between 1 and
     * {@link Integer#MAX_VALUE}.
     *
     * @param readTimeout read timeout in milliseconds
     * @return Api client
     */
    public ApiClient setReadTimeout(int readTimeout) {
        httpClient = httpClient.newBuilder().readTimeout(readTimeout, TimeUnit.MILLISECONDS).build();
        return this;
    }

    /**
     * Get write timeout (in milliseconds).
     *
     * @return Timeout in milliseconds
     */
    public int getWriteTimeout() {
        return httpClient.writeTimeoutMillis();
    }

    /**
     * Sets the write timeout (in milliseconds).
     * A value of 0 means no timeout, otherwise values must be between 1 and
     * {@link Integer#MAX_VALUE}.
     *
     * @param writeTimeout connection timeout in milliseconds
     * @return Api client
     */
    public ApiClient setWriteTimeout(int writeTimeout) {
        httpClient = httpClient.newBuilder().writeTimeout(writeTimeout, TimeUnit.MILLISECONDS).build();
        return this;
    }


    /**
     * Format the given parameter object into string.
     *
     * @param param Parameter
     * @return String representation of the parameter
     */
    public String parameterToString(Object param) {
        if (param == null) {
            return "";
        } else if (param instanceof Date || param instanceof OffsetDateTime || param instanceof LocalDate) {
            //Serialize to json string and remove the " enclosing characters
            String jsonStr = json.serialize(param);
            return jsonStr.substring(1, jsonStr.length() - 1);
        } else if (param instanceof Collection) {
            StringBuilder b = new StringBuilder();
            for (Object o : (Collection) param) {
                if (b.length() > 0) {
                    b.append(",");
                }
                b.append(String.valueOf(o));
            }
            return b.toString();
        } else {
            return String.valueOf(param);
        }
    }

    /**
     * Formats the specified query parameter to a list containing a single {@code Pair} object.
     *
     * Note that {@code value} must not be a collection.
     *
     * @param name The name of the parameter.
     * @param value The value of the parameter.
     * @return A list containing a single {@code Pair} object.
     */
    public List<Pair> parameterToPair(String name, Object value) {
        List<Pair> params = new ArrayList<Pair>();

        // preconditions
        if (name == null || name.isEmpty() || value == null || value instanceof Collection) {
            return params;
        }

        params.add(new Pair(name, parameterToString(value)));
        return params;
    }

    /**
     * Formats the specified collection query parameters to a list of {@code Pair} objects.
     *
     * Note that the values of each of the returned Pair objects are percent-encoded.
     *
     * @param collectionFormat The collection format of the parameter.
     * @param name The name of the parameter.
     * @param value The value of the parameter.
     * @return A list of {@code Pair} objects.
     */
    public List<Pair> parameterToPairs(String collectionFormat, String name, Collection value) {
        List<Pair> params = new ArrayList<Pair>();

        // preconditions
        if (name == null || name.isEmpty() || value == null || value.isEmpty()) {
            return params;
        }

        // create the params based on the collection format
        if ("multi".equals(collectionFormat)) {
            for (Object item : value) {
                params.add(new Pair(name, escapeString(parameterToString(item))));
            }
            return params;
        }

        // collectionFormat is assumed to be "csv" by default
        String delimiter = ",";

        // escape all delimiters except commas, which are URI reserved
        // characters
        if ("ssv".equals(collectionFormat)) {
            delimiter = escapeString(" ");
        } else if ("tsv".equals(collectionFormat)) {
            delimiter = escapeString("\t");
        } else if ("pipes".equals(collectionFormat)) {
            delimiter = escapeString("|");
        }

        StringBuilder sb = new StringBuilder();
        for (Object item : value) {
            sb.append(delimiter);
            sb.append(escapeString(parameterToString(item)));
        }

        params.add(new Pair(name, sb.substring(delimiter.length())));

        return params;
    }

    /**
     * Formats the specified collection path parameter to a string value.
     *
     * @param collectionFormat The collection format of the parameter.
     * @param value The value of the parameter.
     * @return String representation of the parameter
     */
    public String collectionPathParameterToString(String collectionFormat, Collection value) {
        // create the value based on the collection format
        if ("multi".equals(collectionFormat)) {
            // not valid for path params
            return parameterToString(value);
        }

        // collectionFormat is assumed to be "csv" by default
        String delimiter = ",";

        if ("ssv".equals(collectionFormat)) {
            delimiter = " ";
        } else if ("tsv".equals(collectionFormat)) {
            delimiter = "\t";
        } else if ("pipes".equals(collectionFormat)) {
            delimiter = "|";
        }

        StringBuilder sb = new StringBuilder() ;
        for (Object item : value) {
            sb.append(delimiter);
            sb.append(parameterToString(item));
        }

        return sb.substring(delimiter.length());
    }

    /**
     * Sanitize filename by removing path.
     * e.g. ../../sun.gif becomes sun.gif
     *
     * @param filename The filename to be sanitized
     * @return The sanitized filename
     */
    public String sanitizeFilename(String filename) {
        return filename.replaceAll(".*[/\\\\]", "");
    }

    /**
     * Check if the given MIME is a JSON MIME.
     * JSON MIME examples:
     *   application/json
     *   application/json; charset=UTF8
     *   APPLICATION/JSON
     *   application/vnd.company+json
     * "* / *" is also default to JSON
     * @param mime MIME (Multipurpose Internet Mail Extensions)
     * @return True if the given MIME is JSON, false otherwise.
     */
    public boolean isJsonMime(String mime) {
        String jsonMime = "(?i)^(application/json|[^;/ \t]+/[^;/ \t]+[+]json)[ \t]*(;.*)?$";
        return mime != null && (mime.matches(jsonMime) || mime.equals("*/*"));
    }

    /**
     * Select the Accept header's value from the given accepts array:
     *   if JSON exists in the given array, use it;
     *   otherwise use all of them (joining into a string)
     *
     * @param accepts The accepts array to select from
     * @return The Accept header to use. If the given array is empty,
     *   null will be returned (not to set the Accept header explicitly).
     */
    public String selectHeaderAccept(String[] accepts) {
        if (accepts.length == 0) {
            return null;
        }
        for (String accept : accepts) {
            if (isJsonMime(accept)) {
                return accept;
            }
        }
        return StringUtil.join(accepts, ",");
    }

    /**
     * Select the Content-Type header's value from the given array:
     *   if JSON exists in the given array, use it;
     *   otherwise use the first one of the array.
     *
     * @param contentTypes The Content-Type array to select from
     * @return The Content-Type header to use. If the given array is empty,
     *   or matches "any", JSON will be used.
     */
    public String selectHeaderContentType(String[] contentTypes) {
        if (contentTypes.length == 0 || contentTypes[0].equals("*/*")) {
            return "application/json";
        }
        for (String contentType : contentTypes) {
            if (isJsonMime(contentType)) {
                return contentType;
            }
        }
        return contentTypes[0];
    }

    /**
     * Escape the given string to be used as URL query value.
     *
     * @param str String to be escaped
     * @return Escaped string
     */
    public String escapeString(String str) {
        try {
            return URLEncoder.encode(str, "utf8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            return str;
        }
    }

    /**
     * Deserialize response body to Java object, according to the return type and
     * the Content-Type response header.
     *
     * @param <T> Type
     * @param response HTTP response
     * @param returnType The type of the Java object
     * @return The deserialized Java object
     * @throws ApiException If fail to deserialize response body, i.e. cannot read response body
     *   or the Content-Type of the response is not supported.
     */
    @SuppressWarnings("unchecked")
    public <T> T deserialize(Response response, Type returnType) throws ApiException {
        if (response == null || returnType == null) {
            return null;
        }

        if ("byte[]".equals(returnType.toString())) {
            // Handle binary response (byte array).
            try {
                return (T) response.body().bytes();
            } catch (IOException e) {
                throw new ApiException(e);
            }
        } else if (returnType.equals(File.class)) {
            // Handle file downloading.
            return (T) downloadFileFromResponse(response);
        }

        String respBody;
        try {
            if (response.body() != null)
                respBody = response.body().string();
            else
                respBody = null;
        } catch (IOException e) {
            throw new ApiException(e);
        }

        if (respBody == null || "".equals(respBody)) {
            return null;
        }

        String contentType = response.headers().get("Content-Type");
        if (contentType == null) {
            // ensuring a default content type
            contentType = "application/json";
        }
        if (isJsonMime(contentType)) {
            return json.deserialize(respBody, returnType);
        } else if (returnType.equals(String.class)) {
            // Expecting string, return the raw response body.
            return (T) respBody;
        } else {
            throw new ApiException(
                    "Content type \"" + contentType + "\" is not supported for type: " + returnType,
                    response.code(),
                    response.headers().toMultimap(),
                    respBody);
        }
    }

    /**
     * Serialize the given Java object into request body according to the object's
     * class and the request Content-Type.
     *
     * @param obj The Java object
     * @param contentType The request Content-Type
     * @return The serialized request body
     * @throws ApiException If fail to serialize the given object
     */
    public RequestBody serialize(Object obj, String contentType) throws ApiException {
        if (obj instanceof byte[]) {
            // Binary (byte array) body parameter support.
            return RequestBody.create((byte[]) obj, MediaType.parse(contentType));
        } else if (obj instanceof File) {
            // File body parameter support.
            return RequestBody.create((File) obj, MediaType.parse(contentType));
        } else if (isJsonMime(contentType)) {
            String content;
            if (obj != null) {
                content = json.serialize(obj);
            } else {
                content = null;
            }
            return RequestBody.create(content, MediaType.parse(contentType));
        } else {
            throw new ApiException("Content type \"" + contentType + "\" is not supported");
        }
    }

    /**
     * Download file from the given response.
     *
     * @param response An instance of the Response object
     * @throws ApiException If fail to read file content from response and write to disk
     * @return Downloaded file
     */
    public File downloadFileFromResponse(Response response) throws ApiException {
        try {
            File file = prepareDownloadFile(response);
            BufferedSink sink = Okio.buffer(Okio.sink(file));
            sink.writeAll(response.body().source());
            sink.close();
            return file;
        } catch (IOException e) {
            throw new ApiException(e);
        }
    }

    /**
     * Prepare file for download
     *
     * @param response An instance of the Response object
     * @return Prepared file for the download
     * @throws IOException If fail to prepare file for download
     */
    public File prepareDownloadFile(Response response) throws IOException {
        String filename = null;
        String contentDisposition = response.header("Content-Disposition");
        if (contentDisposition != null && !"".equals(contentDisposition)) {
            // Get filename from the Content-Disposition header.
            Pattern pattern = Pattern.compile("filename=['\"]?([^'\"\\s]+)['\"]?");
            Matcher matcher = pattern.matcher(contentDisposition);
            if (matcher.find()) {
                filename = sanitizeFilename(matcher.group(1));
            }
        }

        String prefix = null;
        String suffix = null;
        if (filename == null) {
            prefix = "download-";
            suffix = "";
        } else {
            int pos = filename.lastIndexOf(".");
            if (pos == -1) {
                prefix = filename + "-";
            } else {
                prefix = filename.substring(0, pos) + "-";
                suffix = filename.substring(pos);
            }
            // Files.createTempFile requires the prefix to be at least three characters long
            if (prefix.length() < 3)
                prefix = "download-";
        }

        if (tempFolderPath == null)
            return Files.createTempFile(prefix, suffix).toFile();
        else
            return Files.createTempFile(Paths.get(tempFolderPath), prefix, suffix).toFile();
    }

    /**
     * {@link #execute(Call, Type)}
     *
     * @param <T> Type
     * @param call An instance of the Call object
     * @return ApiResponse&lt;T&gt;
     * @throws ApiException If fail to execute the call
     */
    public <T> ApiResponse<T> execute(Call call) throws ApiException {
        return execute(call, null);
    }

    /**
     * Execute HTTP call and deserialize the HTTP response body into the given return type.
     *
     * @param returnType The return type used to deserialize HTTP response body
     * @param <T> The return type corresponding to (same with) returnType
     * @param call Call
     * @return ApiResponse object containing response status, headers and
     *   data, which is a Java object deserialized from response body and would be null
     *   when returnType is null.
     * @throws ApiException If fail to execute the call
     */
    public <T> ApiResponse<T> execute(Call call, Type returnType) throws ApiException {
        try {
            Response response = call.execute();
            T data = handleResponse(response, returnType);
            return new ApiResponse<T>(response.code(), response.headers().toMultimap(), data);
        } catch (IOException e) {
            throw new ApiException(e);
        }
    }

    /**
     * {@link #executeAsync(Call, Type, ApiCallback)}
     *
     * @param <T> Type
     * @param call An instance of the Call object
     * @param callback ApiCallback&lt;T&gt;
     */
    public <T> void executeAsync(Call call, ApiCallback<T> callback) {
        executeAsync(call, null, callback);
    }

    /**
     * Execute HTTP call asynchronously.
     *
     * @param <T> Type
     * @param call The callback to be executed when the API call finishes
     * @param returnType Return type
     * @param callback ApiCallback
     * @see #execute(Call, Type)
     */
    @SuppressWarnings("unchecked")
    public <T> void executeAsync(Call call, final Type returnType, final ApiCallback<T> callback) {
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(new ApiException(e), 0, null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                T result;
                try {
                    result = (T) handleResponse(response, returnType);
                } catch (ApiException e) {
                    callback.onFailure(e, response.code(), response.headers().toMultimap());
                    return;
                } catch (Exception e) {
                    callback.onFailure(new ApiException(e), response.code(), response.headers().toMultimap());
                    return;
                }
                callback.onSuccess(result, response.code(), response.headers().toMultimap());
            }
        });
    }

    /**
     * Handle the given response, return the deserialized object when the response is successful.
     *
     * @param <T> Type
     * @param response Response
     * @param returnType Return type
     * @return Type
     * @throws ApiException If the response has an unsuccessful status code or
     *                      fail to deserialize the response body
     */
    public <T> T handleResponse(Response response, Type returnType) throws ApiException {
        if (response.isSuccessful()) {
            if (returnType == null || response.code() == 204) {
                // returning null if the returnType is not defined,
                // or the status code is 204 (No Content)
                if (response.body() != null) {
                    try {
                        response.body().close();
                    } catch (Exception e) {
                        throw new ApiException(response.message(), e, response.code(), response.headers().toMultimap());
                    }
                }
                return null;
            } else {
                return deserialize(response, returnType);
            }
        } else {
            String respBody = null;
            if (response.body() != null) {
                try {
                    respBody = response.body().string();
                } catch (IOException e) {
                    throw new ApiException(response.message(), e, response.code(), response.headers().toMultimap());
                }
            }
            throw new ApiException(response.message(), response.code(), response.headers().toMultimap(), respBody);
        }
    }

    /**
     * Build HTTP call with the given options.
     *
     * @param path The sub-path of the HTTP URL
     * @param method The request method, one of "GET", "HEAD", "OPTIONS", "POST", "PUT", "PATCH" and "DELETE"
     * @param queryParams The query parameters
     * @param collectionQueryParams The collection query parameters
     * @param body The request body object
     * @param headerParams The header parameters
     * @param cookieParams The cookie parameters
     * @param formParams The form parameters
     * @param authNames The authentications to apply
     * @param callback Callback for upload/download progress
     * @return The HTTP call
     * @throws ApiException If fail to serialize the request body object
     */
    public Call buildCall(String path, String method, List<Pair> queryParams, List<Pair> collectionQueryParams, Object body, Map<String, String> headerParams, Map<String, String> cookieParams, Map<String, Object> formParams, String[] authNames, ApiCallback callback) throws ApiException {
        Request request = buildRequest(path, method, queryParams, collectionQueryParams, body, headerParams, cookieParams, formParams, authNames, callback);

        return httpClient.newCall(request);
    }

    /**
     * Build an HTTP request with the given options.
     *
     * @param path The sub-path of the HTTP URL
     * @param method The request method, one of "GET", "HEAD", "OPTIONS", "POST", "PUT", "PATCH" and "DELETE"
     * @param queryParams The query parameters
     * @param collectionQueryParams The collection query parameters
     * @param body The request body object
     * @param headerParams The header parameters
     * @param cookieParams The cookie parameters
     * @param formParams The form parameters
     * @param authNames The authentications to apply
     * @param callback Callback for upload/download progress
     * @return The HTTP request
     * @throws ApiException If fail to serialize the request body object
     */
    public Request buildRequest(String path, String method, List<Pair> queryParams, List<Pair> collectionQueryParams, Object body, Map<String, String> headerParams, Map<String, String> cookieParams, Map<String, Object> formParams, String[] authNames, ApiCallback callback) throws ApiException {
        updateParamsForAuth(authNames, queryParams, headerParams, cookieParams);

        final String url = buildUrl(path, queryParams, collectionQueryParams);
        final Request.Builder reqBuilder = new Request.Builder().url(url);
        processHeaderParams(headerParams, reqBuilder);
        processCookieParams(cookieParams, reqBuilder);

        String contentType = (String) headerParams.get("Content-Type");
        // ensuring a default content type
        if (contentType == null) {
            contentType = "application/json";
        }

        RequestBody reqBody;
        if (!HttpMethod.permitsRequestBody(method)) {
            reqBody = null;
        } else if ("application/x-www-form-urlencoded".equals(contentType)) {
            reqBody = buildRequestBodyFormEncoding(formParams);
        } else if ("multipart/form-data".equals(contentType)) {
            reqBody = buildRequestBodyMultipart(formParams);
        } else if (body == null) {
            if ("DELETE".equals(method)) {
                // allow calling DELETE without sending a request body
                reqBody = null;
            } else {
                // use an empty request body (for POST, PUT and PATCH)
                reqBody = RequestBody.create("", MediaType.parse(contentType));
            }
        } else {
            reqBody = serialize(body, contentType);
        }

        // Associate callback with request (if not null) so interceptor can
        // access it when creating ProgressResponseBody
        reqBuilder.tag(callback);

        Request request = null;

        if (callback != null && reqBody != null) {
            ProgressRequestBody progressRequestBody = new ProgressRequestBody(reqBody, callback);
            request = reqBuilder.method(method, progressRequestBody).build();
        } else {
            request = reqBuilder.method(method, reqBody).build();
        }

        return request;
    }

    /**
     * Build full URL by concatenating base path, the given sub path and query parameters.
     *
     * @param path The sub path
     * @param queryParams The query parameters
     * @param collectionQueryParams The collection query parameters
     * @return The full URL
     */
    public String buildUrl(String path, List<Pair> queryParams, List<Pair> collectionQueryParams) {
        final StringBuilder url = new StringBuilder();
        url.append(basePath).append(path);

        if (queryParams != null && !queryParams.isEmpty()) {
            // support (constant) query string in `path`, e.g. "/posts?draft=1"
            String prefix = path.contains("?") ? "&" : "?";
            for (Pair param : queryParams) {
                if (param.getValue() != null) {
                    if (prefix != null) {
                        url.append(prefix);
                        prefix = null;
                    } else {
                        url.append("&");
                    }
                    String value = parameterToString(param.getValue());
                    url.append(escapeString(param.getName())).append("=").append(escapeString(value));
                }
            }
        }

        if (collectionQueryParams != null && !collectionQueryParams.isEmpty()) {
            String prefix = url.toString().contains("?") ? "&" : "?";
            for (Pair param : collectionQueryParams) {
                if (param.getValue() != null) {
                    if (prefix != null) {
                        url.append(prefix);
                        prefix = null;
                    } else {
                        url.append("&");
                    }
                    String value = parameterToString(param.getValue());
                    // collection query parameter value already escaped as part of parameterToPairs
                    url.append(escapeString(param.getName())).append("=").append(value);
                }
            }
        }

        return url.toString();
    }

    /**
     * Set header parameters to the request builder, including default headers.
     *
     * @param headerParams Header parameters in the form of Map
     * @param reqBuilder Request.Builder
     */
    public void processHeaderParams(Map<String, String> headerParams, Request.Builder reqBuilder) {
        for (Entry<String, String> param : headerParams.entrySet()) {
            reqBuilder.header(param.getKey(), parameterToString(param.getValue()));
        }
        for (Entry<String, String> header : defaultHeaderMap.entrySet()) {
            if (!headerParams.containsKey(header.getKey())) {
                reqBuilder.header(header.getKey(), parameterToString(header.getValue()));
            }
        }
    }

    /**
     * Set cookie parameters to the request builder, including default cookies.
     *
     * @param cookieParams Cookie parameters in the form of Map
     * @param reqBuilder Request.Builder
     */
    public void processCookieParams(Map<String, String> cookieParams, Request.Builder reqBuilder) {
        for (Entry<String, String> param : cookieParams.entrySet()) {
            reqBuilder.addHeader("Cookie", String.format("%s=%s", param.getKey(), param.getValue()));
        }
        for (Entry<String, String> param : defaultCookieMap.entrySet()) {
            if (!cookieParams.containsKey(param.getKey())) {
                reqBuilder.addHeader("Cookie", String.format("%s=%s", param.getKey(), param.getValue()));
            }
        }
    }

    /**
     * Update query and header parameters based on authentication settings.
     *
     * @param authNames The authentications to apply
     * @param queryParams List of query parameters
     * @param headerParams Map of header parameters
     * @param cookieParams Map of cookie parameters
     */
    public void updateParamsForAuth(String[] authNames, List<Pair> queryParams, Map<String, String> headerParams, Map<String, String> cookieParams) {
        for (String authName : authNames) {
            Authentication auth = authentications.get(authName);
            if (auth == null) {
                throw new RuntimeException("Authentication undefined: " + authName);
            }
            auth.applyToParams(queryParams, headerParams, cookieParams);
        }
    }

    /**
     * Build a form-encoding request body with the given form parameters.
     *
     * @param formParams Form parameters in the form of Map
     * @return RequestBody
     */
    public RequestBody buildRequestBodyFormEncoding(Map<String, Object> formParams) {
        okhttp3.FormBody.Builder formBuilder = new okhttp3.FormBody.Builder();
        for (Entry<String, Object> param : formParams.entrySet()) {
            formBuilder.add(param.getKey(), parameterToString(param.getValue()));
        }
        return formBuilder.build();
    }

    /**
     * Build a multipart (file uploading) request body with the given form parameters,
     * which could contain text fields and file fields.
     *
     * @param formParams Form parameters in the form of Map
     * @return RequestBody
     */
    public RequestBody buildRequestBodyMultipart(Map<String, Object> formParams) {
        MultipartBody.Builder mpBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (Entry<String, Object> param : formParams.entrySet()) {
            if (param.getValue() instanceof File) {
                File file = (File) param.getValue();
                Headers partHeaders = Headers.of("Content-Disposition", "form-data; name=\"" + param.getKey() + "\"; filename=\"" + file.getName() + "\"");
                MediaType mediaType = MediaType.parse(guessContentTypeFromFile(file));
                mpBuilder.addPart(partHeaders, RequestBody.create(file, mediaType));
            } else {
                Headers partHeaders = Headers.of("Content-Disposition", "form-data; name=\"" + param.getKey() + "\"");
                mpBuilder.addPart(partHeaders, RequestBody.create(parameterToString(param.getValue()), null));
            }
        }
        return mpBuilder.build();
    }

    /**
     * Guess Content-Type header from the given file (defaults to "application/octet-stream").
     *
     * @param file The given file
     * @return The guessed Content-Type
     */
    public String guessContentTypeFromFile(File file) {
        String contentType = URLConnection.guessContentTypeFromName(file.getName());
        if (contentType == null) {
            return "application/octet-stream";
        } else {
            return contentType;
        }
    }

    /**
     * Get network interceptor to add it to the httpClient to track download progress for
     * async requests.
     */
    private Interceptor getProgressInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                final Request request = chain.request();
                final Response originalResponse = chain.proceed(request);
                if (request.tag() instanceof ApiCallback) {
                    final ApiCallback callback = (ApiCallback) request.tag();
                    return originalResponse.newBuilder()
                        .body(new ProgressResponseBody(originalResponse.body(), callback))
                        .build();
                }
                return originalResponse;
            }
        };
    }

    /**
     * Apply SSL related settings to httpClient according to the current values of
     * verifyingSsl and sslCaCert.
     */
    private void applySslSettings() {
        try {
            TrustManager[] trustManagers;
            HostnameVerifier hostnameVerifier;
            if (!verifyingSsl) {
                trustManagers = new TrustManager[]{
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return new java.security.cert.X509Certificate[]{};
                            }
                        }
                };
                hostnameVerifier = new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                };
            } else {
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

                if (sslCaCert == null) {
                    trustManagerFactory.init((KeyStore) null);
                } else {
                    char[] password = null; // Any password will work.
                    CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                    Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(sslCaCert);
                    if (certificates.isEmpty()) {
                        throw new IllegalArgumentException("expected non-empty set of trusted certificates");
                    }
                    KeyStore caKeyStore = newEmptyKeyStore(password);
                    int index = 0;
                    for (Certificate certificate : certificates) {
                        String certificateAlias = "ca" + Integer.toString(index++);
                        caKeyStore.setCertificateEntry(certificateAlias, certificate);
                    }
                    trustManagerFactory.init(caKeyStore);
                }
                trustManagers = trustManagerFactory.getTrustManagers();
                hostnameVerifier = OkHostnameVerifier.INSTANCE;
            }

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagers, trustManagers, new SecureRandom());
            httpClient = httpClient.newBuilder()
                            .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustManagers[0])
                            .hostnameVerifier(hostnameVerifier)
                            .build();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    private KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, password);
            return keyStore;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }
}
