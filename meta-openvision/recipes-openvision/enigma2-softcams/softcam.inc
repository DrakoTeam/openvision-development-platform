# To create a softcam:
# - install binary to ${bindir}/
# - install other stuff wherever you need it
# - fill in (mandatory)
#   CAMNAME: Name of softcam, preferably matches ${bindir} name
# - fill in (optional)
#   CAMSTART: Command to start the softcam, default to ${bindir}/${CAMNAME}
#   CAMSTOP: Command to stop the softcam, default to killall ${CAMNAME}
# Instead of CAMSTART/CAMSTOP, you can create an init.d script called init.${CAMNAME}

LICENSE = "CLOSED"

FILES_${PN} = "${bindir} ${sysconfdir}"

# no docs, locales etc.
PACKAGES = "${PN}"

RDEPENDS_${PN} += "softcam-support"
INSANE_SKIP_${PN} += "already-stripped"

CAMLINK = "$D${sysconfdir}/init.d/softcam"
CAMPATH = "$D${sysconfdir}/init.d/softcam.${CAMNAME}"

CAMSTART ?= "exec start-stop-daemon -S -x ${bindir}/${CAMNAME}"
CAMSTOP  ?= "exec start-stop-daemon -K -R 2 -x ${bindir}/${CAMNAME}"

# Generate a simplistic standard init script
# (sorry for the sleep 1, but start-stop-daemon -R does not work as advertised)
do_compile_append () {
	if [ ! -f init.${CAMNAME} ]
	then
		cat > init.${CAMNAME} << EOF
#!/bin/sh
case "\$1" in
start)
	ulimit -s 1024
	${CAMSTART}
	;;
stop)
	${CAMSTOP}
	;;
restart|reload)
	\$0 stop
	sleep 1
	\$0 start
	;;
version)
	echo "${PV}"
	;;
info)
	echo "${CAMNAME} ${PV}"
	;;
*)
	echo "Usage: \$0 start|stop|restart"
	exit 1
	;;
esac
exit 0
EOF

	fi
}

# Install routine, should be ok for most cams.
do_install_append () {
	install -d ${D}${sysconfdir}/init.d
	install -m 755 ${S}/init.${CAMNAME} ${D}${sysconfdir}/init.d/softcam.${CAMNAME}
}

# If no cam selected yet, install and start this cam (and don't start it on the build host).
pkg_postinst_${PN}_append () {
	if [ ! -e "${CAMLINK}" ] || [ "${sysconfdir}/init.d/softcam.None" = "`readlink -f ${CAMLINK}`" ]
	then
		rm -f "${CAMLINK}"
		ln -s "softcam.${CAMNAME}" "${CAMLINK}"
		echo "Switching default softcam to ${CAMNAME}"
	fi
	if [ "${CAMPATH}" = "`readlink -f ${CAMLINK}`" ]
	then
		echo "Softcam is selected as default, (re)starting ${CAMNAME}"
		${CAMPATH} restart
	fi
}

# Stop this cam (if running), and move softlink to None if we're the current cam
pkg_prerm_${PN}_prepend () {
	${CAMPATH} stop
	OLDLINK="`readlink -f ${CAMLINK}`"
	if [ "${OLDLINK}" = "${CAMPATH}" ]
	then
		echo "${CAMNAME} was selected, now selecting None as softcam"
		rm -f "${CAMLINK}"
		ln -s "softcam.None" "${CAMLINK}"
	fi
}
