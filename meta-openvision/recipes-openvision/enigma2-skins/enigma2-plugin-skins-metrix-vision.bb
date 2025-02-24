SUMMARY = "Enigma2 Skin MetrixHD"
MAINTAINER = "Open Vision Developers"
SECTION = "base"
PRIORITY = "required"
LICENSE = "proprietary"

require conf/license/license-gplv2.inc

inherit gitpkgv gettext allarch

DEPENDS += "gettext-native"
#RDEPENDS_${PN} = "python-imaging"

PV = "2.2+git${SRCPV}"
PKGV = "2.2+git${GITPKGV}"

PACKAGES += "enigma2-plugin-skins-metrix-vision-fhd-icons enigma2-plugin-skins-metrix-vision-uhd-icons"
PROVIDES += "enigma2-plugin-skins-metrix-vision-fhd-icons enigma2-plugin-skins-metrix-vision-uhd-icons"
RPROVIDES_enigma2-plugin-skins-metrix-vision-fhd-icons += "enigma2-plugin-skins-metrix-vision-fhd-icons"
RPROVIDES_enigma2-plugin-skins-metrix-vision-uhd-icons += "enigma2-plugin-skins-metrix-vision-uhd-icons"

SRC_URI="git://github.com/OpenVisionE2/MetrixHD-skin.git"

S = "${WORKDIR}/git"

FILES_enigma2-plugin-skins-metrix-vision-fhd-icons = "${datadir}/enigma2/MetrixHD/FHD"
FILES_enigma2-plugin-skins-metrix-vision-uhd-icons = "${datadir}/enigma2/MetrixHD/UHD"
FILES_${PN}-src = "\
    ${libdir}/enigma2/python/Components/Converter/*.py \
    ${libdir}/enigma2/python/Components/Renderer/*.py \
    ${libdir}/enigma2/python/Plugins/Extensions/MyMetrixLite/*.py \
    "

FILES_${PN} = "${libdir} ${datadir}"

do_compile() {
	python -O -m compileall ${S}/usr
	for f in $(find ${S}/locale -name *.po ); do
		l=$(echo ${f%} | sed 's/\.po//' | sed 's/.*locale\///')
		mkdir -p ${S}${libdir}/enigma2/python/Plugins/Extensions/MyMetrixLite/locale/${l%}/LC_MESSAGES
		msgfmt -o ${S}${libdir}/enigma2/python/Plugins/Extensions/MyMetrixLite/locale/${l%}/LC_MESSAGES/MyMetrixLite.mo ${S}/locale/$l.po
	done
}

do_install() {
    install -d ${D}${libdir}
    install -d ${D}${datadir}
    cp -r --preserve=mode,links ${S}${libdir}/* ${D}${libdir}/
    cp -r --preserve=mode,links ${S}${datadir}/* ${D}${datadir}/
}

pkg_preinst_${PN}() {
#!/bin/sh
echo "Checking for skin.MySkin.xml in the skinfolder"
if [ -f ${datadir}/enigma2/MetrixHD/skin.MySkin.xml ]; then
    rm -f ${datadir}/enigma2/MetrixHD/skin.MySkin.xml
    echo "skin.MySkin.xml was found and removed"
else
    echo "skin.MySkin.xml was not found in the skinfolder"
fi
echo "Checking for skin_00a_InfoBar.MySkin.xml in the skinfolder"
if [ -f ${datadir}/enigma2/MetrixHD/skin_00a_InfoBar.MySkin.xml ]; then
    rm -f ${datadir}/enigma2/MetrixHD/skin_00a_InfoBar.MySkin.xml
    echo "skin_00a_InfoBar.MySkin.xml was found and removed"
else
    echo "skin_00a_InfoBar.MySkin.xml was not found in the skinfolder"
fi
echo "Checking for skin_00b_SecondInfoBar.MySkin.xml in the skinfolder"
if [ -f ${datadir}/enigma2/MetrixHD/skin_00b_SecondInfoBar.MySkin.xml ]; then
    rm -f ${datadir}/enigma2/MetrixHD/skin_00b_SecondInfoBar.MySkin.xml
    echo "skin_00b_SecondInfoBar.MySkin.xml was found and removed"
else
    echo "skin_00b_SecondInfoBar.MySkin.xml was not found in the skinfolder"
fi
echo "Checking for skin_00c_SecondInfoBarECM.MySkin.xml in the skinfolder"
if [ -f ${datadir}/enigma2/MetrixHD/skin_00c_SecondInfoBarECM.MySkin.xml ]; then
    rm -f ${datadir}/enigma2/MetrixHD/skin_00c_SecondInfoBarECM.MySkin.xml
    echo "skin_00c_SecondInfoBarECM.MySkin.xml was found and removed"
else
    echo "skin_00c_SecondInfoBarECM.MySkin.xml was not found in the skinfolder"
fi
echo "Checking for skin_00d_InfoBarLite.MySkin.xml in the skinfolder"
if [ -f ${datadir}/enigma2/MetrixHD/skin_00d_InfoBarLite.MySkin.xml ]; then
    rm -f ${datadir}/enigma2/MetrixHD/skin_00d_InfoBarLite.MySkin.xml
    echo "skin_00d_InfoBarLite.MySkin.xml was found and removed"
else
    echo "skin_00d_InfoBarLite.MySkin.xml was not found in the skinfolder"
fi
echo "Checking for skin_00e_ChannelSelection.MySkin.xml in the skinfolder"
if [ -f ${datadir}/enigma2/MetrixHD/skin_00e_ChannelSelection.MySkin.xml ]; then
    rm -f ${datadir}/enigma2/MetrixHD/skin_00e_ChannelSelection.MySkin.xml
    echo "skin_00e_ChannelSelection.MySkin.xml was found and removed"
else
    echo "skin_00e_ChannelSelection.MySkin.xml was not found in the skinfolder"
fi
echo "Proceeding to installation..."
exit 0
}

pkg_postinst_${PN} () {
#!/bin/sh
echo "Checking for obsolete MyMetrixLiteColors"
if [ -d ${libdir}/enigma2/python/Plugins/Extensions/MyMetrixLiteColors ]; then
    rm -rf ${libdir}/enigma2/python/Plugins/Extensions/MyMetrixLiteColors 2>/dev/null
    echo "MyMetrixLiteColors was found and removed"
else
    echo "MyMetrixLiteColors was not found"
fi
echo "Proceeding..."
exit 0
}
