# ------------------------------------------------
# to be updated
ST_KERNEL_VERSION = "5.16-rc4"
LINUX_VERSION = "5.16"

SRC_URI = "https://git.kernel.org/torvalds/t/linux-${ST_KERNEL_VERSION}.tar.gz;name=kernel"

SRC_URI[kernel.sha256sum] = "d4dc0a118b391ac594279178ef0217ca69f45dc65d0c5d2fdd8964828f608036"
# ------------------------------------------------
require linux-stm32mp-oss.inc
