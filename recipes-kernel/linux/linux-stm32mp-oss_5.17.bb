# ------------------------------------------------
# to be updated
ST_KERNEL_VERSION = "5.17"
LINUX_VERSION = "5.17"

SRC_URI = "https://git.kernel.org/torvalds/t/linux-${ST_KERNEL_VERSION}.tar.gz;name=kernel"

SRC_URI[kernel.sha256sum] = "f58338c5b06314559a269afe70b33b9b0b75cc2fe5e8a537ac29f70caa6e489b"
# ------------------------------------------------
require linux-stm32mp-oss.inc
