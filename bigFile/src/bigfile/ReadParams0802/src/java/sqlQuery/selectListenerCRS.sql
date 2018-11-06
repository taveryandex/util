declare
crs sys_refcursor;
v_sClob CLOB := Empty_Clob();
i NUMBER(11,0);
    v_Z44STRF NUMBER(11,0);
    v_Z44RQID VARCHAR2(60);
    v_Z44SSID VARCHAR2(60);
    v_Z44RQTP VARCHAR2(60);
    v_Z44SSYN VARCHAR2(80);
    v_Z44DSYN VARCHAR2(80);
    v_Z44CUID VARCHAR2(80);
    v_Z44DLLN VARCHAR2(254);
    v_Z44PMOD NUMBER(2,0);
    v_Z44ATYP VARCHAR2(30);
    v_Z44SERR VARCHAR2(4000);


begin
crs :=rram_handler.listenerCRS(120, 'RetCom_GP_DOCUMENT_QUEUE');
 
loop
fetch crs into  v_sClob, v_Z44STRF, v_Z44RQID, v_Z44SSID, v_Z44RQTP, v_Z44SSYN, v_Z44DSYN, v_Z44CUID, v_Z44DLLN, v_Z44PMOD, v_Z44ATYP, v_Z44SERR;
dbms_output.put_line('  ;;  v_sClob='||DBMS_LOB.substr(v_sClob,4000,1)||'  ;;  v_Z44STRF='||v_Z44STRF||'  ;;  v_Z44RQID='||v_Z44RQID||'  ;;  v_Z44SSID='||v_Z44SSID||'  ;;  v_Z44RQTP='||v_Z44RQTP||'  ;;  v_Z44SSYN='||v_Z44SSYN||'  ;;  v_Z44DSYN='||v_Z44DSYN||'  ;;  v_Z44CUID='||v_Z44CUID||'  ;;  v_Z44DLLN='||v_Z44DLLN||'  ;;  v_Z44PMOD='||v_Z44PMOD||'  ;;  v_Z44ATYP='||v_Z44ATYP||'  ;;  v_Z44SERR='||v_Z44SERR);
dbms_output.put_line(i);
exit when crs%NOTFOUND;
i:=i+1;
end loop;
close crs;
end;
