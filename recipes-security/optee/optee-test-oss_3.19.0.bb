require optee-test-oss.inc
# Need to be aligned with the version of OPTEE-OS
# ------------------------------------------------
# to be updated
OPTEE_VERSION = "3.19.0"

SRCREV = "eb3d01f1e3c9279948895ea79320587dded5c6da"
SRC_URI += "file://0001-no-error-deprecated-declarations.patch"
#SRCREV = "da5282a011b40621a2cf7a296c11a35c833ed91b" # 3.18.0
#SRCREV = "44a31d02379bd8e50762caa5e1592ad81e3339af" # 3.17.0
#SRCREV = "1cf0e6d2bdd1145370033d4e182634458528579d" # 3.16.0
#SRCREV = "f88f69eb27beda52998de09cd89a7ee422da00d9" # 3.15.0
# ------------------------------------------------

