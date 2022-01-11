# ------------------------------------------------
# to be updated
ST_KERNEL_VERSION = "5.16"
LINUX_VERSION = "5.16"

SRC_URI = "https://git.kernel.org/torvalds/t/linux-${ST_KERNEL_VERSION}.tar.gz;name=kernel"

SRC_URI[kernel.sha256sum] = "d5a4751aada0ecd6bee5dbc961dd056e5f8f87ac86dfed9dae145a85035765d2"
# ------------------------------------------------
require linux-stm32mp-oss.inc
