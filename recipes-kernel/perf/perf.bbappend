FILESEXTRAPATHS:append := "${THISDIR}/files:"

DEPENDS += "zstd libcap-ng "

RDEPENDS:${PN}-tests =+ "bash"
PACKAGECONFIG:stm32mpcommon = "scripting tui libunwind"
EXTRA_OEMAKE += " NO_LIBTRACEEVENT=1 "
