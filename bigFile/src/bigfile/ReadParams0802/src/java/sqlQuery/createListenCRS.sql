CREATE OR REPLACE FUNCTION ListenerCRStest
(
    p_nWaitTimeOut              IN      PLS_INTEGER DEFAULT DBMS_ALERT.MAXWAIT, --  Время ожидания в секундах
    p_sSignal                   IN      VARCHAR2 DEFAULT 'SIGNALIZE'
)
RETURN SYS_REFCURSOR
IS
    crsReadyMessage                     SYS_REFCURSOR;
    sClob                               CLOB := Empty_Clob();
 
BEGIN


 
 
  DELETE FROM "RBR"."Z44test" ;
  INSERT INTO "RBR"."Z44test"  (z44clob, z44strf, z44rqid, z44ssid, z44rqtp, z44ssyn, z44dsyn, z44cuid, z44dlln, z44pmod, z44atyp, z44serr)
  VALUES (sCLOB, 1, 'Z44RQID', 'Z44SSID', 'Z44RQTP', 'Z44SSYN', 'Z44DSYN', 'Z44CUID', 'Z44DLLN', 1, 'Z44ATYP', 'Z44SERR');

  OPEN crsReadyMessage
  FOR
      SELECT z44clob AS Message,
             z44strf AS InternalMessageID,
             z44rqid AS RequestID,
             z44ssid AS SessionID,
             z44rqtp AS RequestType,
             z44ssyn AS SourceSystemName,
             z44dsyn AS DestinationSystemName,
             z44cuid AS ClientID,
             z44dlln AS GateName,
             z44pmod AS HandlingMode,
             z44atyp AS AnswerType,
             z44serr AS ErrorMessage
      FROM "RBR"."Z44test" 
      WHERE ROWNUM = 1;

 
  commit; -- для прочистки Z44
  RETURN crsReadyMessage;
END ListenerCRStest;
