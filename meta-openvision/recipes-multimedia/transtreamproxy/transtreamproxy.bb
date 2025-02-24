SUMMARY = "streamproxy manages streaming data to a Mobile device using enigma2"
PRIORITY = "required"
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit gitpkgv autotools

PV = "1.0+git${SRCPV}"
PKGV = "1.0+git${GITPKGV}"

PROVIDES += "virtual/transtreamproxy"
RPROVIDES_${PN} += "virtual/transtreamproxy"

DEPENDS = "boost"

SRC_URI = "git://github.com/OpenVuPlus/filestreamproxy.git;protocol=http;branch=transtreamproxy \
    file://gcc6.patch \
    "

EXTRA_OECONF_vusolo4k += " --enable-ext-pid "
EXTRA_OECONF_vuultimo4k += " --enable-ext-pid "
EXTRA_OECONF_vuuno4k += " --enable-ext-pid "
EXTRA_OECONF_vuuno4kse += " --enable-ext-pid "
EXTRA_OECONF_vuduo4k += " --enable-ext-pid "
EXTRA_OECONF_force3uhdplus += " --enable-ext-pid "
EXTRA_OECONF_force3uhd += " --enable-ext-pid "
EXTRA_OECONF_revo4k += " --enable-ext-pid "
EXTRA_OECONF_tmtwin4k += " --enable-ext-pid "
EXTRA_OECONF_galaxy4k += " --enable-ext-pid "
EXTRA_OECONF_tm4ksuper += " --enable-ext-pid "
EXTRA_OECONF_lunix3-4k += " --enable-ext-pid "
EXTRA_OECONF_gbquad4k += " --enable-ext-pid "
EXTRA_OECONF_gbue4k += " --enable-ext-pid "

S = "${WORKDIR}/git"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/build/src/transtreamproxy ${D}${bindir}/transtreamproxy
}

pkg_prerm_${PN}() {
#!/bin/sh
grep -vE '^#*\s*8002' $D${sysconfdir}/inetd.conf > $D/tmp/inetd.tmp
mv $D/tmp/inetd.tmp $D${sysconfdir}/inetd.conf

if [ -z "$D" -a -f "${sysconfdir}/init.d/inetd.busybox" ]; then
    ${sysconfdir}/init.d/inetd.busybox restart
fi
}

pkg_preinst_${PN}() {
#!/bin/sh
if [ -z "$D" ]; then
    grep -vE '^#*\s*8002' $D${sysconfdir}/inetd.conf > $D/tmp/inetd.tmp
    mv $D/tmp/inetd.tmp $D${sysconfdir}/inetd.conf
fi

if [ -z "$D" -a -f "${sysconfdir}/init.d/inetd.busybox" ]; then
    ${sysconfdir}/init.d/inetd.busybox restart
fi
}

pkg_postinst_${PN}() {
#!/bin/sh
if grep -qE "^#*\s*8003" $D${sysconfdir}/inetd.conf; then
    sed -i "s#^\(\#*\s*8003\)#8002\t\tstream\ttcp6\tnowait\troot\t${bindir}/transtreamproxy\ttranstreamproxy\n\1#" $D${sysconfdir}/inetd.conf
else
    echo -e "8002\t\tstream\ttcp6\tnowait\troot\t${bindir}/transtreamproxy\ttranstreamproxy" >> $D${sysconfdir}/inetd.conf
fi

if [ -z "$D" -a -f "${sysconfdir}/init.d/inetd.busybox" ]; then
    ${sysconfdir}/init.d/inetd.busybox restart
fi
}

pkg_postinst_${PN}_gbquad4k() {
#!/bin/sh
if ! grep -qE '^#*\s*8002' $D${sysconfdir}/inetd.conf; then
    if grep -qE "^#*\s*8003" $D${sysconfdir}/inetd.conf; then
        sed -i "s#^\(\#*\s*8003\)#8002\t\tstream\ttcp6\tnowait\troot\t${bindir}/transtreamproxy\ttranstreamproxy\n\1#" $D${sysconfdir}/inetd.conf
    else
        echo -e "8002\t\tstream\ttcp6\tnowait\troot\t${bindir}/transtreamproxy\ttranstreamproxy" >> $D${sysconfdir}/inetd.conf
    fi
    if [ -z "$D" -a -f "${sysconfdir}/init.d/inetd.busybox" ]; then
        ${sysconfdir}/init.d/inetd.busybox restart
    fi
fi
}

pkg_postinst_${PN}_gbue4k() {
#!/bin/sh
if ! grep -qE '^#*\s*8002' $D${sysconfdir}/inetd.conf; then
    if grep -qE "^#*\s*8003" $D${sysconfdir}/inetd.conf; then
        sed -i "s#^\(\#*\s*8003\)#8002\t\tstream\ttcp6\tnowait\troot\t${bindir}/transtreamproxy\ttranstreamproxy\n\1#" $D${sysconfdir}/inetd.conf
    else
        echo -e "8002\t\tstream\ttcp6\tnowait\troot\t${bindir}/transtreamproxy\ttranstreamproxy" >> $D${sysconfdir}/inetd.conf
    fi
    if [ -z "$D" -a -f "${sysconfdir}/init.d/inetd.busybox" ]; then
        ${sysconfdir}/init.d/inetd.busybox restart
    fi
fi
}
