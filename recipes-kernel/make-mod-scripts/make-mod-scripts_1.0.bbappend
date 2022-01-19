do_configure:append() {
	unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
	for t in modules_prepare; do
		oe_runmake CC="${KERNEL_CC}" LD="${KERNEL_LD}" AR="${KERNEL_AR}" \
		-C ${STAGING_KERNEL_DIR} O=${STAGING_KERNEL_BUILDDIR} $t
	done
}
