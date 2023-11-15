inherit image_types

do_image_stmultiubi[depends] += " \
        mtd-utils-native:do_populate_sysroot \
        bc-native:do_populate_sysroot \
        ${PN}:do_image_stbootfs \
        ${PN}:do_image_ubifs \
        "

python stmultiub_environment () {
    # Get the MULTIUBI_BUILD list without any duplicates
    ubiconfigs = list(dict.fromkeys((d.getVar('MULTIUBI_BUILD') or "").split()))
    if ubiconfigs:
        try:
            f =open( ("%s/stmultiubi_environment" % d.getVar('T')), 'w')
            for build in ubiconfigs:
                # Append 'build' to OVERRIDES
                localdata = bb.data.createCopy(d)
                overrides = localdata.getVar('OVERRIDES')
                if not overrides:
                    bb.fatal('OVERRIDES not defined')
                localdata.setVar('OVERRIDES', build + ':' + overrides)
                # Compute export vars
                f.write( "export MKUBIFS_ARGS_%s=\"%s\"\n" % (build, localdata.getVar('MKUBIFS_ARGS')) )
                f.write( "export UBINIZE_ARGS_%s=\"%s\"\n" % (build, localdata.getVar('UBINIZE_ARGS')) )
                f.write( "export EXTRA_UBIFS_SIZE_%s=\"%s\"\n" % (build, localdata.getVar('EXTRA_UBIFS_SIZE')) )
                f.write( "export STM32MP_UBI_VOLUME_%s=\"%s\"\n" % (build, localdata.getVar('STM32MP_UBI_VOLUME')) )
            f.close()
        except:
            pass
}

IMAGE_PREPROCESS_COMMAND += "stmultiub_environment;"

IMAGE_CMD:stmultiubi () {
    . ${T}/stmultiubi_environment

    st_multivolume_ubifs
}

ENABLE_MULTIVOLUME_UBI ?= "1"

# -----------------------------------------------------------------------------
# Define the list of volumes for the multi UBIFS with 'STM32MP_UBI_VOLUME' var.
# The format to follow is:
#   STM32MP_UBI_VOLUME = "<VOL_NAME_1>:<VOL_SIZE_1>:<VOL_TYPE_1> <VOL_NAME_2>:<VOL_SIZE_2>"
# Note that:
#   - 'VOL_NAME' is the image volume name
#   - 'VOL_SIZE' is set in KiB
#   - 'VOL_TYPE' is optional part and could be 'empty' to add empty UBI with
#     volume name set to 'VOL_NAME'
# -----------------------------------------------------------------------------
STM32MP_UBI_VOLUME ?= ""

st_multivolume_ubifs() {
    if [ "${ENABLE_MULTIVOLUME_UBI}" != "1" ]; then
        return
    fi

    . ${T}/stmultiubi_environment

    for name in ${MULTIUBI_BUILD}; do
        ubinize_cfg=${IMAGE_NAME}_${name}_multivolume.ubinize.cfg.ubi
        # static configuration
        echo "[uboot_config]" > ${WORKDIR}/${ubinize_cfg}
        echo mode=ubi >> ${WORKDIR}/${ubinize_cfg}
        echo vol_id=0 >> ${WORKDIR}/${ubinize_cfg}
        echo vol_type=dynamic >> ${WORKDIR}/${ubinize_cfg}
        echo vol_name=uboot_config >> ${WORKDIR}/${ubinize_cfg}
        echo vol_size=256KiB >> ${WORKDIR}/${ubinize_cfg}
        echo "[uboot_config_r]" >> ${WORKDIR}/${ubinize_cfg}
        echo mode=ubi >> ${WORKDIR}/${ubinize_cfg}
        echo vol_id=1 >> ${WORKDIR}/${ubinize_cfg}
        echo vol_type=dynamic >> ${WORKDIR}/${ubinize_cfg}
        echo vol_name=uboot_config_r >> ${WORKDIR}/${ubinize_cfg}
        echo vol_size=256KiB >> ${WORKDIR}/${ubinize_cfg}
        # bootfs
        echo "[boot]" >> ${WORKDIR}/${ubinize_cfg}
        echo mode=ubi >> ${WORKDIR}/${ubinize_cfg}
        echo vol_id=2 >> ${WORKDIR}/${ubinize_cfg}
        echo image=${IMGDEPLOYDIR}/${IMAGE_NAME}.bootfs.ubifs >> ${WORKDIR}/${ubinize_cfg}
        echo vol_type=dynamic >> ${WORKDIR}/${ubinize_cfg}
        echo vol_name=boot >> ${WORKDIR}/${ubinize_cfg}
        echo vol_size=67840KiB >> ${WORKDIR}/${ubinize_cfg}
        # rootfs
        echo "[rootfs]" >> ${WORKDIR}/${ubinize_cfg}
        echo mode=ubi >> ${WORKDIR}/${ubinize_cfg}
        echo vol_id=3 >> ${WORKDIR}/${ubinize_cfg}
        echo image=${IMGDEPLOYDIR}/${IMAGE_NAME}.ubifs >> ${WORKDIR}/${ubinize_cfg}
        echo vol_type=dynamic >> ${WORKDIR}/${ubinize_cfg}
        echo vol_name=rootfs >> ${WORKDIR}/${ubinize_cfg}
        echo vol_flags=autoresize >> ${WORKDIR}/${ubinize_cfg}
        #echo vol_size=754944KiB >> ${WORKDIR}/${ubinize_cfg}

        cp ${WORKDIR}/${ubinize_cfg} ${IMGDEPLOYDIR}/${ubinize_cfg}

        # Generate multivolume UBI
        eval local ubinize_args=\"\$UBINIZE_ARGS_${name}\"
        bbnote "ubinize -o ${IMGDEPLOYDIR}/${IMAGE_NAME}_${name}_multivolume${IMAGE_NAME_SUFFIX}.ubi ${ubinize_args} ${IMGDEPLOYDIR}/${IMAGE_NAME}_${name}_multivolume.ubinize.cfg.ubi"
        ubinize -o ${IMGDEPLOYDIR}/${IMAGE_NAME}_${name}_multivolume${IMAGE_NAME_SUFFIX}.ubi ${ubinize_args} ${IMGDEPLOYDIR}/${ubinize_cfg}

        # Create own symlinks for 'named' volumes
        cd ${IMGDEPLOYDIR}
        if [ -e ${IMAGE_NAME}_${name}_multivolume${IMAGE_NAME_SUFFIX}.ubi ]; then
            ln -sf ${IMAGE_NAME}_${name}_multivolume${IMAGE_NAME_SUFFIX}.ubi ${IMAGE_LINK_NAME}_${name}_multivolume.ubi
            ln -sf ${IMAGE_NAME}_${name}_multivolume.ubinize.cfg.ubi ${IMAGE_LINK_NAME}_${name}_multivolume.ubinize.cfg.ubi
        fi
        cd -

        # Cleanup also DEPLOY_DIR_IMAGE from any other ubi artifacts
        # This avoid duplicating data in DEPLOY_DIR_IMAGE
        rm -f ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}-*_${name}_multivolume${IMAGE_NAME_SUFFIX}.ubi
        rm -f ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}-*_${name}_multivolume.ubinize.cfg.ubi
    done
}

# -----------------------------------------------------------------------------
# Manage specific var dependency:
# Because of local overrides within st_multivolume_ubifs() function, we
# need to make sure to add each variables to the vardeps list.
MULTIUBI_LABELS_VARS = "MKUBIFS_ARGS UBINIZE_ARGS EXTRA_UBIFS_SIZE STM32MP_UBI_VOLUME"
MULTIUBI_LABELS_OVERRIDES = "${MULTIUBI_BUILD}"
stmultiub_environment[vardeps] += "${@' '.join(['%s_%s' % (v, o) for v in d.getVar('MULTIUBI_LABELS_VARS').split() for o in d.getVar('MULTIUBI_LABELS_OVERRIDES').split()])}"

