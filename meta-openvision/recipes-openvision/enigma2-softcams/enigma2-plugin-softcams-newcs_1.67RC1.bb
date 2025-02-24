DESCRIPTION = "newcs ${PV} cardserver"

SRC_URI = "http://downloads.openpli.org/softcams/newcs-${PV}.zip\
	http://downloads.pli-images.org/softcams/newcs.xml;name=xml"

S = "${WORKDIR}/newcs-1.67_RC1"

INHIBIT_PACKAGE_STRIP = "1"

CSNAME = "newcs"
CSSTART = "exec start-stop-daemon -S -b -x ${bindir}${CSNAME}"

require cardserver.inc

do_install() {
	install -d ${D}${bindir}
	install -m 0755 ${S}/bin/newcs.mips ${D}${bindir}/newcs
	install -d ${D}${sysconfdir}/tuxbox/config
	install -m 0644 ${WORKDIR}/newcs.xml ${D}${sysconfdir}/tuxbox/config/newcs.xml.example
}

pkg_postinst_${PN} () {
	[ -e $D${sysconfdir}/tuxbox/config/newcs.xml ] || cp $D${sysconfdir}/tuxbox/config/newcs.xml.example $D${sysconfdir}/tuxbox/config/newcs.xml
}

SRC_URI[md5sum] = "0a9b6826159090fece84ac6927dae264"
SRC_URI[sha256sum] = "db4d4f24479c5429e363c359baac1111d673b93f8971056bb4c5243fbb80b946"

SRC_URI[xml.md5sum] = "08539a72f32816a0b69d5c39611d734e"
SRC_URI[xml.sha256sum] = "8b699264bb94dc2f2b640dd30c37aa2cbaf969b04774ec4df606af99bb56fa90"
