DESCRIPTION = "Tooling for devicetree validation using YAML and jsonschema"
HOMEPAGE = "https://github.com/devicetree-org/dt-schema"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=457495c8fa03540db4a576bf7869e811"

DEPENDS = "python3-jsonschema python3-ruamel-yaml python3-rfc3987"

inherit setuptools3

SRC_URI = "git://github.com/devicetree-org/dt-schema.git;protocol=https;nobranch=1"
SRCREV = "9be4deb854bcbc31ef0454260456be29f59eaeeb"
PV = "0.1+git${SRCPV}"

S = "${WORKDIR}/git"
BBCLASSEXTEND = "native nativesdk"
