DESCRIPTION = "TI DSPLIB Optimised TMS320C674x DSP Library"
HOMEPAGE = "http://focus.ti.com/docs/toolsw/folders/print/sprc900.html"
SECTION = "multimedia"

# TODO : Add variants for 67x/674x/etc
# TODO : Add compile step to enable rebuild

PV = "1_10"
PVwithdots = "1.10"

SRC_URI[dsplibgz.md5sum] = "1335cd16485045590fd85806a566abc3"
SRC_URI[dsplibgz.sha256sum] = "911532495cccc3bd9705a1da5bb154445c4e5127c06492f7f276c2fe543803c9"

PR = "r1"

require ti-paths.inc
require ti-staging.inc
require ti-eula-unpack.inc

S = "${WORKDIR}/c674x/dsplib_v11"

SRC_URI = "http://www.ti.com/litv/gz/sprc906a;name=dsplibgz"

#Later this will have dependencies when we rebuild the libraries/examples
#DEPENDS = "ti-cgt6x ti-xdctools ti-dspbios ti-codec-engine" 

PRETARFILE="sprc906a"
BINFILE="c674x_dsplib_v11-Linux-x86-Install"
TI_BIN_UNPK_CMDS="Y:workdir:"

python do_unpack () {
    bb.build.exec_func('base_do_unpack', d)
    bb.build.exec_func('ti_pretar_do_unpack', d)
    bb.build.exec_func('ti_bin_do_unpack', d)
}

python ti_pretar_do_unpack() {

    import os

    localdata = bb.data.createCopy(d)
    bb.data.update_data(localdata)

    # Change to the working directory
    save_cwd = os.getcwd()
    workdir  = bb.data.getVar('WORKDIR', localdata)
    workdir  = bb.data.expand(workdir, localdata)
    os.chdir(workdir)

    # Expand the tarball that was created if required
    tarfile  = bb.data.getVar('PRETARFILE', localdata)    
    if bool(tarfile) == True:
        tarfile  = bb.data.expand(tarfile, localdata)
        tcmd = 'tar x --no-same-owner -f %s -C %s' % (tarfile, workdir)
        os.system(tcmd)

    # Return to the previous directory
    os.chdir(save_cwd)
}

do_prepsources() {
    echo "Do Nothing for Now" 
}

addtask prepsources after do_configure before do_compile

do_compile() {
    echo "Do Nothing for Now"
}

do_install() {

    install -d ${D}${DSPLIB_INSTALL_DIR_RECIPE}
	sed -i 's,\\,/,g' ${S}/dsplib674x.h
    cp -pPrf ${S}/* ${D}${DSPLIB_INSTALL_DIR_RECIPE}
}





