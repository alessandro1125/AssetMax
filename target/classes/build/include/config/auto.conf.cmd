deps_config := \
	/home/aless/esp/esp-idf/components/app_trace/Kconfig \
	/home/aless/esp/esp-idf/components/aws_iot/Kconfig \
	/home/aless/esp/esp-idf/components/bt/Kconfig \
	/home/aless/esp/esp-idf/components/driver/Kconfig \
	/home/aless/esp/esp-idf/components/esp32/Kconfig \
	/home/aless/esp/esp-idf/components/esp_adc_cal/Kconfig \
	/home/aless/esp/esp-idf/components/esp_http_client/Kconfig \
	/home/aless/esp/esp-idf/components/ethernet/Kconfig \
	/home/aless/esp/esp-idf/components/fatfs/Kconfig \
	/home/aless/esp/esp-idf/components/freertos/Kconfig \
	/home/aless/esp/esp-idf/components/heap/Kconfig \
	/home/aless/esp/esp-idf/components/libsodium/Kconfig \
	/home/aless/esp/esp-idf/components/log/Kconfig \
	/home/aless/esp/esp-idf/components/lwip/Kconfig \
	/home/aless/esp/esp-idf/components/mbedtls/Kconfig \
	/home/aless/esp/esp-idf/components/openssl/Kconfig \
	/home/aless/esp/esp-idf/components/pthread/Kconfig \
	/home/aless/esp/esp-idf/components/spi_flash/Kconfig \
	/home/aless/esp/esp-idf/components/spiffs/Kconfig \
	/home/aless/esp/esp-idf/components/tcpip_adapter/Kconfig \
	/home/aless/esp/esp-idf/components/wear_levelling/Kconfig \
	/home/aless/esp/esp-idf/components/bootloader/Kconfig.projbuild \
	/home/aless/esp/esp-idf/components/esptool_py/Kconfig.projbuild \
	/home/aless/esp/esp-idf/components/partition_table/Kconfig.projbuild \
	/home/aless/esp/esp-idf/Kconfig

include/config/auto.conf: \
	$(deps_config)


$(deps_config): ;
