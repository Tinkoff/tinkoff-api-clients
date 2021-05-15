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


package ru.tinkoff.api.salary;

import ru.tinkoff.api.ApiCallback;
import ru.tinkoff.api.ApiClient;
import ru.tinkoff.api.ApiException;
import ru.tinkoff.api.ApiResponse;
import ru.tinkoff.api.Configuration;
import ru.tinkoff.api.Pair;
import ru.tinkoff.api.ProgressRequestBody;
import ru.tinkoff.api.ProgressResponseBody;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;


import ru.tinkoff.api.salary.model.AuthenticationFailedResponse;
import ru.tinkoff.api.salary.model.AuthorizationFailedResponse;
import ru.tinkoff.api.salary.model.BusinessErrorResponse;
import ru.tinkoff.api.salary.model.CreateEmployeeResultResponse;
import ru.tinkoff.api.salary.model.CreateEmployeesRequest;
import ru.tinkoff.api.salary.model.CreateEmployeesResponse;
import ru.tinkoff.api.salary.model.CreatePaymentRegistryRequest;
import ru.tinkoff.api.salary.model.CreatePaymentRegistryResponse;
import ru.tinkoff.api.salary.model.CreatePaymentRegistryResultResponse;
import ru.tinkoff.api.salary.model.EmployeeResponse;
import ru.tinkoff.api.salary.model.EmployeesInfoRequest;
import ru.tinkoff.api.salary.model.InternalServerErrorResponse;
import ru.tinkoff.api.salary.model.InvalidRequestResponse;
import ru.tinkoff.api.salary.model.PaymentRegistry;
import ru.tinkoff.api.salary.model.PaymentRegistrySubmitRequest;
import ru.tinkoff.api.salary.model.PaymentRegistrySubmitResponse;
import ru.tinkoff.api.salary.model.PaymentRegistrySubmitResultResponse;
import ru.tinkoff.api.salary.model.TooManyRequestsErrorResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SalaryApi {
    private ApiClient localVarApiClient;

    public SalaryApi() {
        this(Configuration.getDefaultApiClient());
    }

    public SalaryApi(ApiClient apiClient) {
        this.localVarApiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return localVarApiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.localVarApiClient = apiClient;
    }

    /**
     * Build call for salaryCreateEmployee
     * @param createEmployeesRequest  (required)
     * @param _callback Callback for upload/download progress
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> Некорректный запрос </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Ошибка аутентификации </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Ошибка авторизации </td><td>  -  </td></tr>
        <tr><td> 422 </td><td> Ошибка при обработке данных </td><td>  -  </td></tr>
        <tr><td> 429 </td><td> Слишком много запросов </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Ошибка сервера </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call salaryCreateEmployeeCall(CreateEmployeesRequest createEmployeesRequest, final ApiCallback _callback) throws ApiException {
        Object localVarPostBody = createEmployeesRequest;

        // create path and map variables
        String localVarPath = "/api/v1/salary/employees/create";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        String[] localVarAuthNames = new String[] { "httpAuth" };
        return localVarApiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call salaryCreateEmployeeValidateBeforeCall(CreateEmployeesRequest createEmployeesRequest, final ApiCallback _callback) throws ApiException {
        
        // verify the required parameter 'createEmployeesRequest' is set
        if (createEmployeesRequest == null) {
            throw new ApiException("Missing the required parameter 'createEmployeesRequest' when calling salaryCreateEmployee(Async)");
        }
        

        okhttp3.Call localVarCall = salaryCreateEmployeeCall(createEmployeesRequest, _callback);
        return localVarCall;

    }

    /**
     * Создать черновики анкет сотрудников
     *  Запрос является асинхронной операцией — его результат можно получить, вызвав метод &lt;a href&#x3D;\&quot;#operation/SalaryGetEmployeesCreateResult\&quot;&gt;Получить результат создания сотрудников&lt;/a&gt;, передав в него соответствующий &lt;b&gt;correlationId&lt;/b&gt;. В поле scope у токена должен присутствовать доступ вида opensme/inn/[{inn}]/kpp/[{kpp}]/salary/employees/manage, где {inn} — ИНН клиента, а {kpp} — КПП клиента.         
     * @param createEmployeesRequest  (required)
     * @return CreateEmployeesResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> Некорректный запрос </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Ошибка аутентификации </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Ошибка авторизации </td><td>  -  </td></tr>
        <tr><td> 422 </td><td> Ошибка при обработке данных </td><td>  -  </td></tr>
        <tr><td> 429 </td><td> Слишком много запросов </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Ошибка сервера </td><td>  -  </td></tr>
     </table>
     */
    public CreateEmployeesResponse salaryCreateEmployee(CreateEmployeesRequest createEmployeesRequest) throws ApiException {
        ApiResponse<CreateEmployeesResponse> localVarResp = salaryCreateEmployeeWithHttpInfo(createEmployeesRequest);
        return localVarResp.getData();
    }

    /**
     * Создать черновики анкет сотрудников
     *  Запрос является асинхронной операцией — его результат можно получить, вызвав метод &lt;a href&#x3D;\&quot;#operation/SalaryGetEmployeesCreateResult\&quot;&gt;Получить результат создания сотрудников&lt;/a&gt;, передав в него соответствующий &lt;b&gt;correlationId&lt;/b&gt;. В поле scope у токена должен присутствовать доступ вида opensme/inn/[{inn}]/kpp/[{kpp}]/salary/employees/manage, где {inn} — ИНН клиента, а {kpp} — КПП клиента.         
     * @param createEmployeesRequest  (required)
     * @return ApiResponse&lt;CreateEmployeesResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> Некорректный запрос </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Ошибка аутентификации </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Ошибка авторизации </td><td>  -  </td></tr>
        <tr><td> 422 </td><td> Ошибка при обработке данных </td><td>  -  </td></tr>
        <tr><td> 429 </td><td> Слишком много запросов </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Ошибка сервера </td><td>  -  </td></tr>
     </table>
     */
    public ApiResponse<CreateEmployeesResponse> salaryCreateEmployeeWithHttpInfo(CreateEmployeesRequest createEmployeesRequest) throws ApiException {
        okhttp3.Call localVarCall = salaryCreateEmployeeValidateBeforeCall(createEmployeesRequest, null);
        Type localVarReturnType = new TypeToken<CreateEmployeesResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    /**
     * Создать черновики анкет сотрудников (asynchronously)
     *  Запрос является асинхронной операцией — его результат можно получить, вызвав метод &lt;a href&#x3D;\&quot;#operation/SalaryGetEmployeesCreateResult\&quot;&gt;Получить результат создания сотрудников&lt;/a&gt;, передав в него соответствующий &lt;b&gt;correlationId&lt;/b&gt;. В поле scope у токена должен присутствовать доступ вида opensme/inn/[{inn}]/kpp/[{kpp}]/salary/employees/manage, где {inn} — ИНН клиента, а {kpp} — КПП клиента.         
     * @param createEmployeesRequest  (required)
     * @param _callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> Некорректный запрос </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Ошибка аутентификации </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Ошибка авторизации </td><td>  -  </td></tr>
        <tr><td> 422 </td><td> Ошибка при обработке данных </td><td>  -  </td></tr>
        <tr><td> 429 </td><td> Слишком много запросов </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Ошибка сервера </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call salaryCreateEmployeeAsync(CreateEmployeesRequest createEmployeesRequest, final ApiCallback<CreateEmployeesResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = salaryCreateEmployeeValidateBeforeCall(createEmployeesRequest, _callback);
        Type localVarReturnType = new TypeToken<CreateEmployeesResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }
    /**
     * Build call for salaryCreatePaymentRegistry
     * @param createPaymentRegistryRequest  (required)
     * @param _callback Callback for upload/download progress
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> Некорректный запрос </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Ошибка аутентификации </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Ошибка авторизации </td><td>  -  </td></tr>
        <tr><td> 422 </td><td> Ошибка при обработке данных </td><td>  -  </td></tr>
        <tr><td> 429 </td><td> Слишком много запросов </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Ошибка сервера </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call salaryCreatePaymentRegistryCall(CreatePaymentRegistryRequest createPaymentRegistryRequest, final ApiCallback _callback) throws ApiException {
        Object localVarPostBody = createPaymentRegistryRequest;

        // create path and map variables
        String localVarPath = "/api/v1/salary/payment-registry/create";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        String[] localVarAuthNames = new String[] { "httpAuth" };
        return localVarApiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call salaryCreatePaymentRegistryValidateBeforeCall(CreatePaymentRegistryRequest createPaymentRegistryRequest, final ApiCallback _callback) throws ApiException {
        
        // verify the required parameter 'createPaymentRegistryRequest' is set
        if (createPaymentRegistryRequest == null) {
            throw new ApiException("Missing the required parameter 'createPaymentRegistryRequest' when calling salaryCreatePaymentRegistry(Async)");
        }
        

        okhttp3.Call localVarCall = salaryCreatePaymentRegistryCall(createPaymentRegistryRequest, _callback);
        return localVarCall;

    }

    /**
     * Создание черновика платежного реестра
     *  Запрос является асинхронной операцией — его результат можно получить, вызвав метод &lt;a href&#x3D;\&quot;#operation/SalaryGetPaymentRegistryCreateResult\&quot;&gt;Получить результат создания платежного реестра&lt;/a&gt;, передав в него соответствующий &lt;b&gt;correlationId&lt;/b&gt;.&lt;br&gt; &lt;u&gt;Вы можете добавлять в реестр сотрудников, которые находятся в статусах ACTIVE и FIRED&lt;/u&gt;&lt;br&gt; В поле scope у токена должен присутствовать доступ вида opensme/inn/[{inn}]/kpp/[{kpp}]/salary/payment-registry/manage, где {inn} — ИНН клиента, а {kpp} — КПП клиента.         
     * @param createPaymentRegistryRequest  (required)
     * @return CreatePaymentRegistryResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> Некорректный запрос </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Ошибка аутентификации </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Ошибка авторизации </td><td>  -  </td></tr>
        <tr><td> 422 </td><td> Ошибка при обработке данных </td><td>  -  </td></tr>
        <tr><td> 429 </td><td> Слишком много запросов </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Ошибка сервера </td><td>  -  </td></tr>
     </table>
     */
    public CreatePaymentRegistryResponse salaryCreatePaymentRegistry(CreatePaymentRegistryRequest createPaymentRegistryRequest) throws ApiException {
        ApiResponse<CreatePaymentRegistryResponse> localVarResp = salaryCreatePaymentRegistryWithHttpInfo(createPaymentRegistryRequest);
        return localVarResp.getData();
    }

    /**
     * Создание черновика платежного реестра
     *  Запрос является асинхронной операцией — его результат можно получить, вызвав метод &lt;a href&#x3D;\&quot;#operation/SalaryGetPaymentRegistryCreateResult\&quot;&gt;Получить результат создания платежного реестра&lt;/a&gt;, передав в него соответствующий &lt;b&gt;correlationId&lt;/b&gt;.&lt;br&gt; &lt;u&gt;Вы можете добавлять в реестр сотрудников, которые находятся в статусах ACTIVE и FIRED&lt;/u&gt;&lt;br&gt; В поле scope у токена должен присутствовать доступ вида opensme/inn/[{inn}]/kpp/[{kpp}]/salary/payment-registry/manage, где {inn} — ИНН клиента, а {kpp} — КПП клиента.         
     * @param createPaymentRegistryRequest  (required)
     * @return ApiResponse&lt;CreatePaymentRegistryResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> Некорректный запрос </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Ошибка аутентификации </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Ошибка авторизации </td><td>  -  </td></tr>
        <tr><td> 422 </td><td> Ошибка при обработке данных </td><td>  -  </td></tr>
        <tr><td> 429 </td><td> Слишком много запросов </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Ошибка сервера </td><td>  -  </td></tr>
     </table>
     */
    public ApiResponse<CreatePaymentRegistryResponse> salaryCreatePaymentRegistryWithHttpInfo(CreatePaymentRegistryRequest createPaymentRegistryRequest) throws ApiException {
        okhttp3.Call localVarCall = salaryCreatePaymentRegistryValidateBeforeCall(createPaymentRegistryRequest, null);
        Type localVarReturnType = new TypeToken<CreatePaymentRegistryResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    /**
     * Создание черновика платежного реестра (asynchronously)
     *  Запрос является асинхронной операцией — его результат можно получить, вызвав метод &lt;a href&#x3D;\&quot;#operation/SalaryGetPaymentRegistryCreateResult\&quot;&gt;Получить результат создания платежного реестра&lt;/a&gt;, передав в него соответствующий &lt;b&gt;correlationId&lt;/b&gt;.&lt;br&gt; &lt;u&gt;Вы можете добавлять в реестр сотрудников, которые находятся в статусах ACTIVE и FIRED&lt;/u&gt;&lt;br&gt; В поле scope у токена должен присутствовать доступ вида opensme/inn/[{inn}]/kpp/[{kpp}]/salary/payment-registry/manage, где {inn} — ИНН клиента, а {kpp} — КПП клиента.         
     * @param createPaymentRegistryRequest  (required)
     * @param _callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> Некорректный запрос </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Ошибка аутентификации </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Ошибка авторизации </td><td>  -  </td></tr>
        <tr><td> 422 </td><td> Ошибка при обработке данных </td><td>  -  </td></tr>
        <tr><td> 429 </td><td> Слишком много запросов </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Ошибка сервера </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call salaryCreatePaymentRegistryAsync(CreatePaymentRegistryRequest createPaymentRegistryRequest, final ApiCallback<CreatePaymentRegistryResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = salaryCreatePaymentRegistryValidateBeforeCall(createPaymentRegistryRequest, _callback);
        Type localVarReturnType = new TypeToken<CreatePaymentRegistryResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }
    /**
     * Build call for salaryGetEmployeesCreateResult
     * @param correlationId Уникальный идентификатор(тип &lt;a href&#x3D;\&quot;https://en.wikipedia.org/wiki/Universally_unique_identifier\&quot;&gt;UUID&lt;/a&gt;), связывающий запрос создания с запросом получения ответа (должен быть сформирован на стороне клиента) (required)
     * @param _callback Callback for upload/download progress
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> Некорректный запрос </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Ошибка аутентификации </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Ошибка авторизации </td><td>  -  </td></tr>
        <tr><td> 422 </td><td> Ошибка при обработке данных </td><td>  -  </td></tr>
        <tr><td> 429 </td><td> Слишком много запросов </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Ошибка сервера </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call salaryGetEmployeesCreateResultCall(String correlationId, final ApiCallback _callback) throws ApiException {
        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/api/v1/salary/employees/create/result";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (correlationId != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("correlationId", correlationId));
        }

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
            
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        String[] localVarAuthNames = new String[] { "httpAuth" };
        return localVarApiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call salaryGetEmployeesCreateResultValidateBeforeCall(String correlationId, final ApiCallback _callback) throws ApiException {
        
        // verify the required parameter 'correlationId' is set
        if (correlationId == null) {
            throw new ApiException("Missing the required parameter 'correlationId' when calling salaryGetEmployeesCreateResult(Async)");
        }
        

        okhttp3.Call localVarCall = salaryGetEmployeesCreateResultCall(correlationId, _callback);
        return localVarCall;

    }

    /**
     * Получить результат создания черновиков анкет сотрудников
     *  Метод возвращает результат запроса на &lt;a href&#x3D;\&quot;#operation/SalaryCreateEmployee\&quot;&gt;создание черновиков анкет сотрудников&lt;/a&gt;. Ответ на запрос создания хранится в течение 2-х дней.&lt;br&gt; В поле scope у токена должен присутствовать доступ вида opensme/inn/[{inn}]/kpp/[{kpp}]/salary/employees/manage, где {inn} — ИНН клиента, а {kpp} — КПП клиента.         
     * @param correlationId Уникальный идентификатор(тип &lt;a href&#x3D;\&quot;https://en.wikipedia.org/wiki/Universally_unique_identifier\&quot;&gt;UUID&lt;/a&gt;), связывающий запрос создания с запросом получения ответа (должен быть сформирован на стороне клиента) (required)
     * @return CreateEmployeeResultResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> Некорректный запрос </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Ошибка аутентификации </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Ошибка авторизации </td><td>  -  </td></tr>
        <tr><td> 422 </td><td> Ошибка при обработке данных </td><td>  -  </td></tr>
        <tr><td> 429 </td><td> Слишком много запросов </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Ошибка сервера </td><td>  -  </td></tr>
     </table>
     */
    public CreateEmployeeResultResponse salaryGetEmployeesCreateResult(String correlationId) throws ApiException {
        ApiResponse<CreateEmployeeResultResponse> localVarResp = salaryGetEmployeesCreateResultWithHttpInfo(correlationId);
        return localVarResp.getData();
    }

    /**
     * Получить результат создания черновиков анкет сотрудников
     *  Метод возвращает результат запроса на &lt;a href&#x3D;\&quot;#operation/SalaryCreateEmployee\&quot;&gt;создание черновиков анкет сотрудников&lt;/a&gt;. Ответ на запрос создания хранится в течение 2-х дней.&lt;br&gt; В поле scope у токена должен присутствовать доступ вида opensme/inn/[{inn}]/kpp/[{kpp}]/salary/employees/manage, где {inn} — ИНН клиента, а {kpp} — КПП клиента.         
     * @param correlationId Уникальный идентификатор(тип &lt;a href&#x3D;\&quot;https://en.wikipedia.org/wiki/Universally_unique_identifier\&quot;&gt;UUID&lt;/a&gt;), связывающий запрос создания с запросом получения ответа (должен быть сформирован на стороне клиента) (required)
     * @return ApiResponse&lt;CreateEmployeeResultResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> Некорректный запрос </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Ошибка аутентификации </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Ошибка авторизации </td><td>  -  </td></tr>
        <tr><td> 422 </td><td> Ошибка при обработке данных </td><td>  -  </td></tr>
        <tr><td> 429 </td><td> Слишком много запросов </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Ошибка сервера </td><td>  -  </td></tr>
     </table>
     */
    public ApiResponse<CreateEmployeeResultResponse> salaryGetEmployeesCreateResultWithHttpInfo(String correlationId) throws ApiException {
        okhttp3.Call localVarCall = salaryGetEmployeesCreateResultValidateBeforeCall(correlationId, null);
        Type localVarReturnType = new TypeToken<CreateEmployeeResultResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    /**
     * Получить результат создания черновиков анкет сотрудников (asynchronously)
     *  Метод возвращает результат запроса на &lt;a href&#x3D;\&quot;#operation/SalaryCreateEmployee\&quot;&gt;создание черновиков анкет сотрудников&lt;/a&gt;. Ответ на запрос создания хранится в течение 2-х дней.&lt;br&gt; В поле scope у токена должен присутствовать доступ вида opensme/inn/[{inn}]/kpp/[{kpp}]/salary/employees/manage, где {inn} — ИНН клиента, а {kpp} — КПП клиента.         
     * @param correlationId Уникальный идентификатор(тип &lt;a href&#x3D;\&quot;https://en.wikipedia.org/wiki/Universally_unique_identifier\&quot;&gt;UUID&lt;/a&gt;), связывающий запрос создания с запросом получения ответа (должен быть сформирован на стороне клиента) (required)
     * @param _callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> Некорректный запрос </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Ошибка аутентификации </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Ошибка авторизации </td><td>  -  </td></tr>
        <tr><td> 422 </td><td> Ошибка при обработке данных </td><td>  -  </td></tr>
        <tr><td> 429 </td><td> Слишком много запросов </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Ошибка сервера </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call salaryGetEmployeesCreateResultAsync(String correlationId, final ApiCallback<CreateEmployeeResultResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = salaryGetEmployeesCreateResultValidateBeforeCall(correlationId, _callback);
        Type localVarReturnType = new TypeToken<CreateEmployeeResultResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }
    /**
     * Build call for salaryGetEmployeesList
     * @param employeesInfoRequest Список идентификаторов сотрудников (required)
     * @param _callback Callback for upload/download progress
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> Некорректный запрос </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Ошибка аутентификации </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Ошибка авторизации </td><td>  -  </td></tr>
        <tr><td> 422 </td><td> Ошибка при обработке данных </td><td>  -  </td></tr>
        <tr><td> 429 </td><td> Слишком много запросов </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Ошибка сервера </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call salaryGetEmployeesListCall(EmployeesInfoRequest employeesInfoRequest, final ApiCallback _callback) throws ApiException {
        Object localVarPostBody = employeesInfoRequest;

        // create path and map variables
        String localVarPath = "/api/v1/salary/employees/list";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        String[] localVarAuthNames = new String[] { "httpAuth" };
        return localVarApiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call salaryGetEmployeesListValidateBeforeCall(EmployeesInfoRequest employeesInfoRequest, final ApiCallback _callback) throws ApiException {
        
        // verify the required parameter 'employeesInfoRequest' is set
        if (employeesInfoRequest == null) {
            throw new ApiException("Missing the required parameter 'employeesInfoRequest' when calling salaryGetEmployeesList(Async)");
        }
        

        okhttp3.Call localVarCall = salaryGetEmployeesListCall(employeesInfoRequest, _callback);
        return localVarCall;

    }

    /**
     * Получить информацию по сотрудникам
     *  Метод вызывается для получения информации по сотрудникам. Вызывать необходимо не чаще, чем раз в 10 минут.&lt;br&gt; Заявка для добавления сотрудника создается в статусе DRAFT, после чего ее необходимо отправить в Личном кабинете. Отправленный сотрудник перейдет в статус ACTIVE.&lt;br&gt; В поле scope у токена должен присутствовать доступ вида opensme/inn/[{inn}]/kpp/[{kpp}]/salary/employees/manage, где {inn} — ИНН клиента, а {kpp} — КПП клиента.         
     * @param employeesInfoRequest Список идентификаторов сотрудников (required)
     * @return EmployeeResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> Некорректный запрос </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Ошибка аутентификации </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Ошибка авторизации </td><td>  -  </td></tr>
        <tr><td> 422 </td><td> Ошибка при обработке данных </td><td>  -  </td></tr>
        <tr><td> 429 </td><td> Слишком много запросов </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Ошибка сервера </td><td>  -  </td></tr>
     </table>
     */
    public EmployeeResponse salaryGetEmployeesList(EmployeesInfoRequest employeesInfoRequest) throws ApiException {
        ApiResponse<EmployeeResponse> localVarResp = salaryGetEmployeesListWithHttpInfo(employeesInfoRequest);
        return localVarResp.getData();
    }

    /**
     * Получить информацию по сотрудникам
     *  Метод вызывается для получения информации по сотрудникам. Вызывать необходимо не чаще, чем раз в 10 минут.&lt;br&gt; Заявка для добавления сотрудника создается в статусе DRAFT, после чего ее необходимо отправить в Личном кабинете. Отправленный сотрудник перейдет в статус ACTIVE.&lt;br&gt; В поле scope у токена должен присутствовать доступ вида opensme/inn/[{inn}]/kpp/[{kpp}]/salary/employees/manage, где {inn} — ИНН клиента, а {kpp} — КПП клиента.         
     * @param employeesInfoRequest Список идентификаторов сотрудников (required)
     * @return ApiResponse&lt;EmployeeResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> Некорректный запрос </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Ошибка аутентификации </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Ошибка авторизации </td><td>  -  </td></tr>
        <tr><td> 422 </td><td> Ошибка при обработке данных </td><td>  -  </td></tr>
        <tr><td> 429 </td><td> Слишком много запросов </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Ошибка сервера </td><td>  -  </td></tr>
     </table>
     */
    public ApiResponse<EmployeeResponse> salaryGetEmployeesListWithHttpInfo(EmployeesInfoRequest employeesInfoRequest) throws ApiException {
        okhttp3.Call localVarCall = salaryGetEmployeesListValidateBeforeCall(employeesInfoRequest, null);
        Type localVarReturnType = new TypeToken<EmployeeResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    /**
     * Получить информацию по сотрудникам (asynchronously)
     *  Метод вызывается для получения информации по сотрудникам. Вызывать необходимо не чаще, чем раз в 10 минут.&lt;br&gt; Заявка для добавления сотрудника создается в статусе DRAFT, после чего ее необходимо отправить в Личном кабинете. Отправленный сотрудник перейдет в статус ACTIVE.&lt;br&gt; В поле scope у токена должен присутствовать доступ вида opensme/inn/[{inn}]/kpp/[{kpp}]/salary/employees/manage, где {inn} — ИНН клиента, а {kpp} — КПП клиента.         
     * @param employeesInfoRequest Список идентификаторов сотрудников (required)
     * @param _callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> Некорректный запрос </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Ошибка аутентификации </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Ошибка авторизации </td><td>  -  </td></tr>
        <tr><td> 422 </td><td> Ошибка при обработке данных </td><td>  -  </td></tr>
        <tr><td> 429 </td><td> Слишком много запросов </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Ошибка сервера </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call salaryGetEmployeesListAsync(EmployeesInfoRequest employeesInfoRequest, final ApiCallback<EmployeeResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = salaryGetEmployeesListValidateBeforeCall(employeesInfoRequest, _callback);
        Type localVarReturnType = new TypeToken<EmployeeResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }
    /**
     * Build call for salaryGetPaymentRegistry
     * @param paymentRegistryId Номер платежного реестра (required)
     * @param _callback Callback for upload/download progress
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> Некорректный запрос </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Ошибка аутентификации </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Ошибка авторизации </td><td>  -  </td></tr>
        <tr><td> 422 </td><td> Ошибка при обработке данных </td><td>  -  </td></tr>
        <tr><td> 429 </td><td> Слишком много запросов </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Ошибка сервера </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call salaryGetPaymentRegistryCall(Integer paymentRegistryId, final ApiCallback _callback) throws ApiException {
        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/api/v1/salary/payment-registry/{paymentRegistryId}"
            .replaceAll("\\{" + "paymentRegistryId" + "\\}", localVarApiClient.escapeString(paymentRegistryId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
            
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        String[] localVarAuthNames = new String[] { "httpAuth" };
        return localVarApiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call salaryGetPaymentRegistryValidateBeforeCall(Integer paymentRegistryId, final ApiCallback _callback) throws ApiException {
        
        // verify the required parameter 'paymentRegistryId' is set
        if (paymentRegistryId == null) {
            throw new ApiException("Missing the required parameter 'paymentRegistryId' when calling salaryGetPaymentRegistry(Async)");
        }
        

        okhttp3.Call localVarCall = salaryGetPaymentRegistryCall(paymentRegistryId, _callback);
        return localVarCall;

    }

    /**
     * Получение платежного реестра
     *  Метод вызывается для получения информации по платежному реестру. Вызывать необходимо не чаще, чем раз в 10 минут.&lt;br&gt; В поле scope у токена должен присутствовать доступ вида opensme/inn/[{inn}]/kpp/[{kpp}]/salary/payment-registry/manage, где {inn} — ИНН клиента, а {kpp} — КПП клиента.         
     * @param paymentRegistryId Номер платежного реестра (required)
     * @return PaymentRegistry
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> Некорректный запрос </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Ошибка аутентификации </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Ошибка авторизации </td><td>  -  </td></tr>
        <tr><td> 422 </td><td> Ошибка при обработке данных </td><td>  -  </td></tr>
        <tr><td> 429 </td><td> Слишком много запросов </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Ошибка сервера </td><td>  -  </td></tr>
     </table>
     */
    public PaymentRegistry salaryGetPaymentRegistry(Integer paymentRegistryId) throws ApiException {
        ApiResponse<PaymentRegistry> localVarResp = salaryGetPaymentRegistryWithHttpInfo(paymentRegistryId);
        return localVarResp.getData();
    }

    /**
     * Получение платежного реестра
     *  Метод вызывается для получения информации по платежному реестру. Вызывать необходимо не чаще, чем раз в 10 минут.&lt;br&gt; В поле scope у токена должен присутствовать доступ вида opensme/inn/[{inn}]/kpp/[{kpp}]/salary/payment-registry/manage, где {inn} — ИНН клиента, а {kpp} — КПП клиента.         
     * @param paymentRegistryId Номер платежного реестра (required)
     * @return ApiResponse&lt;PaymentRegistry&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> Некорректный запрос </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Ошибка аутентификации </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Ошибка авторизации </td><td>  -  </td></tr>
        <tr><td> 422 </td><td> Ошибка при обработке данных </td><td>  -  </td></tr>
        <tr><td> 429 </td><td> Слишком много запросов </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Ошибка сервера </td><td>  -  </td></tr>
     </table>
     */
    public ApiResponse<PaymentRegistry> salaryGetPaymentRegistryWithHttpInfo(Integer paymentRegistryId) throws ApiException {
        okhttp3.Call localVarCall = salaryGetPaymentRegistryValidateBeforeCall(paymentRegistryId, null);
        Type localVarReturnType = new TypeToken<PaymentRegistry>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    /**
     * Получение платежного реестра (asynchronously)
     *  Метод вызывается для получения информации по платежному реестру. Вызывать необходимо не чаще, чем раз в 10 минут.&lt;br&gt; В поле scope у токена должен присутствовать доступ вида opensme/inn/[{inn}]/kpp/[{kpp}]/salary/payment-registry/manage, где {inn} — ИНН клиента, а {kpp} — КПП клиента.         
     * @param paymentRegistryId Номер платежного реестра (required)
     * @param _callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> Некорректный запрос </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Ошибка аутентификации </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Ошибка авторизации </td><td>  -  </td></tr>
        <tr><td> 422 </td><td> Ошибка при обработке данных </td><td>  -  </td></tr>
        <tr><td> 429 </td><td> Слишком много запросов </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Ошибка сервера </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call salaryGetPaymentRegistryAsync(Integer paymentRegistryId, final ApiCallback<PaymentRegistry> _callback) throws ApiException {

        okhttp3.Call localVarCall = salaryGetPaymentRegistryValidateBeforeCall(paymentRegistryId, _callback);
        Type localVarReturnType = new TypeToken<PaymentRegistry>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }
    /**
     * Build call for salaryGetPaymentRegistryCreateResult
     * @param correlationId Идентификатор, связывающий запрос создания с запросом получения ответа (required)
     * @param _callback Callback for upload/download progress
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> Некорректный запрос </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Ошибка аутентификации </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Ошибка авторизации </td><td>  -  </td></tr>
        <tr><td> 422 </td><td> Ошибка при обработке данных </td><td>  -  </td></tr>
        <tr><td> 429 </td><td> Слишком много запросов </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Ошибка сервера </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call salaryGetPaymentRegistryCreateResultCall(String correlationId, final ApiCallback _callback) throws ApiException {
        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/api/v1/salary/payment-registry/create/result";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (correlationId != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("correlationId", correlationId));
        }

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
            
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        String[] localVarAuthNames = new String[] { "httpAuth" };
        return localVarApiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call salaryGetPaymentRegistryCreateResultValidateBeforeCall(String correlationId, final ApiCallback _callback) throws ApiException {
        
        // verify the required parameter 'correlationId' is set
        if (correlationId == null) {
            throw new ApiException("Missing the required parameter 'correlationId' when calling salaryGetPaymentRegistryCreateResult(Async)");
        }
        

        okhttp3.Call localVarCall = salaryGetPaymentRegistryCreateResultCall(correlationId, _callback);
        return localVarCall;

    }

    /**
     * Получить результат создания черновика платежного реестра
     *  Метод возвращает результат запроса на &lt;a href&#x3D;\&quot;#operation/SalaryCreatePaymentRegistry\&quot;&gt;создание платежного реестра&lt;/a&gt;. Результат создания хранится в течение 2-х дней.&lt;br&gt; В поле scope у токена должен присутствовать доступ вида opensme/inn/[{inn}]/kpp/[{kpp}]/salary/payment-registry/manage, где {inn} — ИНН клиента, а {kpp} — КПП клиента.         
     * @param correlationId Идентификатор, связывающий запрос создания с запросом получения ответа (required)
     * @return CreatePaymentRegistryResultResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> Некорректный запрос </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Ошибка аутентификации </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Ошибка авторизации </td><td>  -  </td></tr>
        <tr><td> 422 </td><td> Ошибка при обработке данных </td><td>  -  </td></tr>
        <tr><td> 429 </td><td> Слишком много запросов </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Ошибка сервера </td><td>  -  </td></tr>
     </table>
     */
    public CreatePaymentRegistryResultResponse salaryGetPaymentRegistryCreateResult(String correlationId) throws ApiException {
        ApiResponse<CreatePaymentRegistryResultResponse> localVarResp = salaryGetPaymentRegistryCreateResultWithHttpInfo(correlationId);
        return localVarResp.getData();
    }

    /**
     * Получить результат создания черновика платежного реестра
     *  Метод возвращает результат запроса на &lt;a href&#x3D;\&quot;#operation/SalaryCreatePaymentRegistry\&quot;&gt;создание платежного реестра&lt;/a&gt;. Результат создания хранится в течение 2-х дней.&lt;br&gt; В поле scope у токена должен присутствовать доступ вида opensme/inn/[{inn}]/kpp/[{kpp}]/salary/payment-registry/manage, где {inn} — ИНН клиента, а {kpp} — КПП клиента.         
     * @param correlationId Идентификатор, связывающий запрос создания с запросом получения ответа (required)
     * @return ApiResponse&lt;CreatePaymentRegistryResultResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> Некорректный запрос </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Ошибка аутентификации </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Ошибка авторизации </td><td>  -  </td></tr>
        <tr><td> 422 </td><td> Ошибка при обработке данных </td><td>  -  </td></tr>
        <tr><td> 429 </td><td> Слишком много запросов </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Ошибка сервера </td><td>  -  </td></tr>
     </table>
     */
    public ApiResponse<CreatePaymentRegistryResultResponse> salaryGetPaymentRegistryCreateResultWithHttpInfo(String correlationId) throws ApiException {
        okhttp3.Call localVarCall = salaryGetPaymentRegistryCreateResultValidateBeforeCall(correlationId, null);
        Type localVarReturnType = new TypeToken<CreatePaymentRegistryResultResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    /**
     * Получить результат создания черновика платежного реестра (asynchronously)
     *  Метод возвращает результат запроса на &lt;a href&#x3D;\&quot;#operation/SalaryCreatePaymentRegistry\&quot;&gt;создание платежного реестра&lt;/a&gt;. Результат создания хранится в течение 2-х дней.&lt;br&gt; В поле scope у токена должен присутствовать доступ вида opensme/inn/[{inn}]/kpp/[{kpp}]/salary/payment-registry/manage, где {inn} — ИНН клиента, а {kpp} — КПП клиента.         
     * @param correlationId Идентификатор, связывающий запрос создания с запросом получения ответа (required)
     * @param _callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> Некорректный запрос </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Ошибка аутентификации </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Ошибка авторизации </td><td>  -  </td></tr>
        <tr><td> 422 </td><td> Ошибка при обработке данных </td><td>  -  </td></tr>
        <tr><td> 429 </td><td> Слишком много запросов </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Ошибка сервера </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call salaryGetPaymentRegistryCreateResultAsync(String correlationId, final ApiCallback<CreatePaymentRegistryResultResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = salaryGetPaymentRegistryCreateResultValidateBeforeCall(correlationId, _callback);
        Type localVarReturnType = new TypeToken<CreatePaymentRegistryResultResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }
    /**
     * Build call for salaryPaymentRegistrySubmit
     * @param paymentRegistrySubmitRequest  (required)
     * @param _callback Callback for upload/download progress
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> Некорректный запрос </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Ошибка аутентификации </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Ошибка авторизации </td><td>  -  </td></tr>
        <tr><td> 422 </td><td> Ошибка при обработке данных </td><td>  -  </td></tr>
        <tr><td> 429 </td><td> Слишком много запросов </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Ошибка сервера </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call salaryPaymentRegistrySubmitCall(PaymentRegistrySubmitRequest paymentRegistrySubmitRequest, final ApiCallback _callback) throws ApiException {
        Object localVarPostBody = paymentRegistrySubmitRequest;

        // create path and map variables
        String localVarPath = "/api/v1/salary/payment-registry/submit";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        String[] localVarAuthNames = new String[] { "httpAuth" };
        return localVarApiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call salaryPaymentRegistrySubmitValidateBeforeCall(PaymentRegistrySubmitRequest paymentRegistrySubmitRequest, final ApiCallback _callback) throws ApiException {
        
        // verify the required parameter 'paymentRegistrySubmitRequest' is set
        if (paymentRegistrySubmitRequest == null) {
            throw new ApiException("Missing the required parameter 'paymentRegistrySubmitRequest' when calling salaryPaymentRegistrySubmit(Async)");
        }
        

        okhttp3.Call localVarCall = salaryPaymentRegistrySubmitCall(paymentRegistrySubmitRequest, _callback);
        return localVarCall;

    }

    /**
     * 🔒 Подписание платёжного реестра сотрудников
     *  Запрос является асинхронной операцией — его результат можно получить, вызвав метод &lt;a href&#x3D;\&quot;#operation/SalaryPaymentRegistrySubmitResult\&quot;&gt;Получить результат подписания платежного реестра&lt;/a&gt;, передав в него соответствующий &lt;b&gt;correlationId&lt;/b&gt;.&lt;br&gt; Данный метод позволяет подписать черновик платёжного реестра. createPaymentRegistryПодписанный реестр исполняется после совершения платежа на сумму реестра на транзитный счёт - &lt;b&gt;47422810900000081042&lt;/b&gt;. Создать и оплатить платёжное поручение можно методом &lt;a href&#x3D;\&quot;#operation/PaymentsCorePay\&quot;&gt;Выполнить платёж&lt;/a&gt;. В назначении необходимо указать слово \&quot;реестр\&quot; и номер реестра, который будет исполнен платежом. Например: &lt;em&gt;\&quot;Оплата по договору согласно реестру №394 от 24.09.2020. НДС не облагается\&quot;&lt;/em&gt; &lt;br&gt; В поле scope у токена должен присутствовать доступ вида opensme/inn/[{inn}]/kpp/[{kpp}]/salary/payment-registry/submit, где {inn} — ИНН клиента, а {kpp} — КПП клиента.         
     * @param paymentRegistrySubmitRequest  (required)
     * @return PaymentRegistrySubmitResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> Некорректный запрос </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Ошибка аутентификации </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Ошибка авторизации </td><td>  -  </td></tr>
        <tr><td> 422 </td><td> Ошибка при обработке данных </td><td>  -  </td></tr>
        <tr><td> 429 </td><td> Слишком много запросов </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Ошибка сервера </td><td>  -  </td></tr>
     </table>
     */
    public PaymentRegistrySubmitResponse salaryPaymentRegistrySubmit(PaymentRegistrySubmitRequest paymentRegistrySubmitRequest) throws ApiException {
        ApiResponse<PaymentRegistrySubmitResponse> localVarResp = salaryPaymentRegistrySubmitWithHttpInfo(paymentRegistrySubmitRequest);
        return localVarResp.getData();
    }

    /**
     * 🔒 Подписание платёжного реестра сотрудников
     *  Запрос является асинхронной операцией — его результат можно получить, вызвав метод &lt;a href&#x3D;\&quot;#operation/SalaryPaymentRegistrySubmitResult\&quot;&gt;Получить результат подписания платежного реестра&lt;/a&gt;, передав в него соответствующий &lt;b&gt;correlationId&lt;/b&gt;.&lt;br&gt; Данный метод позволяет подписать черновик платёжного реестра. createPaymentRegistryПодписанный реестр исполняется после совершения платежа на сумму реестра на транзитный счёт - &lt;b&gt;47422810900000081042&lt;/b&gt;. Создать и оплатить платёжное поручение можно методом &lt;a href&#x3D;\&quot;#operation/PaymentsCorePay\&quot;&gt;Выполнить платёж&lt;/a&gt;. В назначении необходимо указать слово \&quot;реестр\&quot; и номер реестра, который будет исполнен платежом. Например: &lt;em&gt;\&quot;Оплата по договору согласно реестру №394 от 24.09.2020. НДС не облагается\&quot;&lt;/em&gt; &lt;br&gt; В поле scope у токена должен присутствовать доступ вида opensme/inn/[{inn}]/kpp/[{kpp}]/salary/payment-registry/submit, где {inn} — ИНН клиента, а {kpp} — КПП клиента.         
     * @param paymentRegistrySubmitRequest  (required)
     * @return ApiResponse&lt;PaymentRegistrySubmitResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> Некорректный запрос </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Ошибка аутентификации </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Ошибка авторизации </td><td>  -  </td></tr>
        <tr><td> 422 </td><td> Ошибка при обработке данных </td><td>  -  </td></tr>
        <tr><td> 429 </td><td> Слишком много запросов </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Ошибка сервера </td><td>  -  </td></tr>
     </table>
     */
    public ApiResponse<PaymentRegistrySubmitResponse> salaryPaymentRegistrySubmitWithHttpInfo(PaymentRegistrySubmitRequest paymentRegistrySubmitRequest) throws ApiException {
        okhttp3.Call localVarCall = salaryPaymentRegistrySubmitValidateBeforeCall(paymentRegistrySubmitRequest, null);
        Type localVarReturnType = new TypeToken<PaymentRegistrySubmitResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    /**
     * 🔒 Подписание платёжного реестра сотрудников (asynchronously)
     *  Запрос является асинхронной операцией — его результат можно получить, вызвав метод &lt;a href&#x3D;\&quot;#operation/SalaryPaymentRegistrySubmitResult\&quot;&gt;Получить результат подписания платежного реестра&lt;/a&gt;, передав в него соответствующий &lt;b&gt;correlationId&lt;/b&gt;.&lt;br&gt; Данный метод позволяет подписать черновик платёжного реестра. createPaymentRegistryПодписанный реестр исполняется после совершения платежа на сумму реестра на транзитный счёт - &lt;b&gt;47422810900000081042&lt;/b&gt;. Создать и оплатить платёжное поручение можно методом &lt;a href&#x3D;\&quot;#operation/PaymentsCorePay\&quot;&gt;Выполнить платёж&lt;/a&gt;. В назначении необходимо указать слово \&quot;реестр\&quot; и номер реестра, который будет исполнен платежом. Например: &lt;em&gt;\&quot;Оплата по договору согласно реестру №394 от 24.09.2020. НДС не облагается\&quot;&lt;/em&gt; &lt;br&gt; В поле scope у токена должен присутствовать доступ вида opensme/inn/[{inn}]/kpp/[{kpp}]/salary/payment-registry/submit, где {inn} — ИНН клиента, а {kpp} — КПП клиента.         
     * @param paymentRegistrySubmitRequest  (required)
     * @param _callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> Некорректный запрос </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Ошибка аутентификации </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Ошибка авторизации </td><td>  -  </td></tr>
        <tr><td> 422 </td><td> Ошибка при обработке данных </td><td>  -  </td></tr>
        <tr><td> 429 </td><td> Слишком много запросов </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Ошибка сервера </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call salaryPaymentRegistrySubmitAsync(PaymentRegistrySubmitRequest paymentRegistrySubmitRequest, final ApiCallback<PaymentRegistrySubmitResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = salaryPaymentRegistrySubmitValidateBeforeCall(paymentRegistrySubmitRequest, _callback);
        Type localVarReturnType = new TypeToken<PaymentRegistrySubmitResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }
    /**
     * Build call for salaryPaymentRegistrySubmitResult
     * @param correlationId Идентификатор, связывающий запрос создания с запросом получения ответа (required)
     * @param _callback Callback for upload/download progress
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> Некорректный запрос </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Ошибка аутентификации </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Ошибка авторизации </td><td>  -  </td></tr>
        <tr><td> 422 </td><td> Ошибка при обработке данных </td><td>  -  </td></tr>
        <tr><td> 429 </td><td> Слишком много запросов </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Ошибка сервера </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call salaryPaymentRegistrySubmitResultCall(String correlationId, final ApiCallback _callback) throws ApiException {
        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/api/v1/salary/payment-registry/submit/result";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (correlationId != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("correlationId", correlationId));
        }

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
            
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        String[] localVarAuthNames = new String[] { "httpAuth" };
        return localVarApiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call salaryPaymentRegistrySubmitResultValidateBeforeCall(String correlationId, final ApiCallback _callback) throws ApiException {
        
        // verify the required parameter 'correlationId' is set
        if (correlationId == null) {
            throw new ApiException("Missing the required parameter 'correlationId' when calling salaryPaymentRegistrySubmitResult(Async)");
        }
        

        okhttp3.Call localVarCall = salaryPaymentRegistrySubmitResultCall(correlationId, _callback);
        return localVarCall;

    }

    /**
     * 🔒 Получить результат подписания платёжного реестра сотрудников
     *  Метод возвращает результат запроса на &lt;a href&#x3D;\&quot;#operation/SalaryPaymentRegistrySubmit\&quot;&gt;подписания платежного реестра&lt;/a&gt;. Результат создания хранится в течение 2-х дней.&lt;br&gt; В поле scope у токена должен присутствовать доступ вида opensme/inn/[{inn}]/kpp/[{kpp}]/salary/payment-registry/submit, где {inn} — ИНН клиента, а {kpp} — КПП клиента.         
     * @param correlationId Идентификатор, связывающий запрос создания с запросом получения ответа (required)
     * @return PaymentRegistrySubmitResultResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> Некорректный запрос </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Ошибка аутентификации </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Ошибка авторизации </td><td>  -  </td></tr>
        <tr><td> 422 </td><td> Ошибка при обработке данных </td><td>  -  </td></tr>
        <tr><td> 429 </td><td> Слишком много запросов </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Ошибка сервера </td><td>  -  </td></tr>
     </table>
     */
    public PaymentRegistrySubmitResultResponse salaryPaymentRegistrySubmitResult(String correlationId) throws ApiException {
        ApiResponse<PaymentRegistrySubmitResultResponse> localVarResp = salaryPaymentRegistrySubmitResultWithHttpInfo(correlationId);
        return localVarResp.getData();
    }

    /**
     * 🔒 Получить результат подписания платёжного реестра сотрудников
     *  Метод возвращает результат запроса на &lt;a href&#x3D;\&quot;#operation/SalaryPaymentRegistrySubmit\&quot;&gt;подписания платежного реестра&lt;/a&gt;. Результат создания хранится в течение 2-х дней.&lt;br&gt; В поле scope у токена должен присутствовать доступ вида opensme/inn/[{inn}]/kpp/[{kpp}]/salary/payment-registry/submit, где {inn} — ИНН клиента, а {kpp} — КПП клиента.         
     * @param correlationId Идентификатор, связывающий запрос создания с запросом получения ответа (required)
     * @return ApiResponse&lt;PaymentRegistrySubmitResultResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> Некорректный запрос </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Ошибка аутентификации </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Ошибка авторизации </td><td>  -  </td></tr>
        <tr><td> 422 </td><td> Ошибка при обработке данных </td><td>  -  </td></tr>
        <tr><td> 429 </td><td> Слишком много запросов </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Ошибка сервера </td><td>  -  </td></tr>
     </table>
     */
    public ApiResponse<PaymentRegistrySubmitResultResponse> salaryPaymentRegistrySubmitResultWithHttpInfo(String correlationId) throws ApiException {
        okhttp3.Call localVarCall = salaryPaymentRegistrySubmitResultValidateBeforeCall(correlationId, null);
        Type localVarReturnType = new TypeToken<PaymentRegistrySubmitResultResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    /**
     * 🔒 Получить результат подписания платёжного реестра сотрудников (asynchronously)
     *  Метод возвращает результат запроса на &lt;a href&#x3D;\&quot;#operation/SalaryPaymentRegistrySubmit\&quot;&gt;подписания платежного реестра&lt;/a&gt;. Результат создания хранится в течение 2-х дней.&lt;br&gt; В поле scope у токена должен присутствовать доступ вида opensme/inn/[{inn}]/kpp/[{kpp}]/salary/payment-registry/submit, где {inn} — ИНН клиента, а {kpp} — КПП клиента.         
     * @param correlationId Идентификатор, связывающий запрос создания с запросом получения ответа (required)
     * @param _callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td>  </td><td>  -  </td></tr>
        <tr><td> 400 </td><td> Некорректный запрос </td><td>  -  </td></tr>
        <tr><td> 401 </td><td> Ошибка аутентификации </td><td>  -  </td></tr>
        <tr><td> 403 </td><td> Ошибка авторизации </td><td>  -  </td></tr>
        <tr><td> 422 </td><td> Ошибка при обработке данных </td><td>  -  </td></tr>
        <tr><td> 429 </td><td> Слишком много запросов </td><td>  -  </td></tr>
        <tr><td> 500 </td><td> Ошибка сервера </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call salaryPaymentRegistrySubmitResultAsync(String correlationId, final ApiCallback<PaymentRegistrySubmitResultResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = salaryPaymentRegistrySubmitResultValidateBeforeCall(correlationId, _callback);
        Type localVarReturnType = new TypeToken<PaymentRegistrySubmitResultResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }
}
