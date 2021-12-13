# ------------------------------------------------
# to be updated
ST_KERNEL_VERSION = "5.16-rc5"
LINUX_VERSION = "5.16"

SRC_URI = "https://git.kernel.org/torvalds/t/linux-${ST_KERNEL_VERSION}.tar.gz;name=kernel"

SRC_URI[kernel.sha256sum] = "42496ba0f464fb7295c38edda1d55369e0c497c24f282d45bebca8ea713b3c4a"

# ------------------------------------------------
require linux-stm32mp-oss.inc
