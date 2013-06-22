$env:DATABASE_DRIVER="org.postgresql.Driver"

$env:DATABASE_URL=(heroku config:get DATABASE_URL) + "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory"

$env:REDISCLOUD_URL=(heroku config:get REDISCLOUD_URL)