# ------------------------------------------------
# to be updated
ST_KERNEL_VERSION = "5.16-rc3"
LINUX_VERSION = "5.16"

SRC_URI = "https://git.kernel.org/torvalds/t/linux-${ST_KERNEL_VERSION}.tar.gz;name=kernel"

SRC_URI[kernel.sha256sum] = "f6ea4727cfd9637ee45af0eec402e67757d6bf295c5ad73a4748c1ee53d828d4"

# ------------------------------------------------
require linux-stm32mp-oss.inc
