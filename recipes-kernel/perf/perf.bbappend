FILESEXTRAPATHS:append := "${THISDIR}/files:"

RDEPENDS:${PN}-tests =+ "bash"

do_configure:append() {
	bbnote "cpr -> ${ST_CANNES2_LAYER_PATH}"

    if [ -e ${STM32MP_OSS_BASE}/recipes-kernel/perf/files/sort-pmuevents.py ]; then
	bbnote "cpr -> ${ST_CANNES2_LAYER_PATH}"
	cp ${STM32MP_OSS_BASE}/recipes-kernel/perf/files/sort-pmuevents.py ${S}
    fi
}
