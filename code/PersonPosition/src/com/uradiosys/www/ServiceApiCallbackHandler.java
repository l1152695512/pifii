
/**
 * ServiceApiCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 19, 2008 (10:13:39 LKT)
 */

    package com.uradiosys.www;

    /**
     *  ServiceApiCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class ServiceApiCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public ServiceApiCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public ServiceApiCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for StartStopLocating method
            * override this method for handling normal response from StartStopLocating operation
            */
           public void receiveResultStartStopLocating(
                    com.uradiosys.www.ServiceApiStub.StartStopLocatingResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from StartStopLocating operation
           */
            public void receiveErrorStartStopLocating(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for SelectAPStatus method
            * override this method for handling normal response from SelectAPStatus operation
            */
           public void receiveResultSelectAPStatus(
                    com.uradiosys.www.ServiceApiStub.SelectAPStatusResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from SelectAPStatus operation
           */
            public void receiveErrorSelectAPStatus(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetTagStatusListAndSummaryAllEventswithUserRight method
            * override this method for handling normal response from GetTagStatusListAndSummaryAllEventswithUserRight operation
            */
           public void receiveResultGetTagStatusListAndSummaryAllEventswithUserRight(
                    com.uradiosys.www.ServiceApiStub.GetTagStatusListAndSummaryAllEventswithUserRightResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetTagStatusListAndSummaryAllEventswithUserRight operation
           */
            public void receiveErrorGetTagStatusListAndSummaryAllEventswithUserRight(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for ClearTagStatus method
            * override this method for handling normal response from ClearTagStatus operation
            */
           public void receiveResultClearTagStatus(
                    com.uradiosys.www.ServiceApiStub.ClearTagStatusResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from ClearTagStatus operation
           */
            public void receiveErrorClearTagStatus(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetMapInfo method
            * override this method for handling normal response from GetMapInfo operation
            */
           public void receiveResultGetMapInfo(
                    com.uradiosys.www.ServiceApiStub.GetMapInfoResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetMapInfo operation
           */
            public void receiveErrorGetMapInfo(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for UpdateHostTagView method
            * override this method for handling normal response from UpdateHostTagView operation
            */
           public void receiveResultUpdateHostTagView(
                    com.uradiosys.www.ServiceApiStub.UpdateHostTagViewResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from UpdateHostTagView operation
           */
            public void receiveErrorUpdateHostTagView(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for ReloadTagHost method
            * override this method for handling normal response from ReloadTagHost operation
            */
           public void receiveResultReloadTagHost(
                    com.uradiosys.www.ServiceApiStub.ReloadTagHostResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from ReloadTagHost operation
           */
            public void receiveErrorReloadTagHost(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetTagStatusListAllEvents method
            * override this method for handling normal response from GetTagStatusListAllEvents operation
            */
           public void receiveResultGetTagStatusListAllEvents(
                    com.uradiosys.www.ServiceApiStub.GetTagStatusListAllEventsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetTagStatusListAllEvents operation
           */
            public void receiveErrorGetTagStatusListAllEvents(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for SelectAPStatusList method
            * override this method for handling normal response from SelectAPStatusList operation
            */
           public void receiveResultSelectAPStatusList(
                    com.uradiosys.www.ServiceApiStub.SelectAPStatusListResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from SelectAPStatusList operation
           */
            public void receiveErrorSelectAPStatusList(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetMapInfoByMapId method
            * override this method for handling normal response from GetMapInfoByMapId operation
            */
           public void receiveResultGetMapInfoByMapId(
                    com.uradiosys.www.ServiceApiStub.GetMapInfoByMapIdResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetMapInfoByMapId operation
           */
            public void receiveErrorGetMapInfoByMapId(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetPositionPointListByMapId method
            * override this method for handling normal response from GetPositionPointListByMapId operation
            */
           public void receiveResultGetPositionPointListByMapId(
                    com.uradiosys.www.ServiceApiStub.GetPositionPointListByMapIdResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetPositionPointListByMapId operation
           */
            public void receiveErrorGetPositionPointListByMapId(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for TestJson method
            * override this method for handling normal response from TestJson operation
            */
           public void receiveResultTestJson(
                    com.uradiosys.www.ServiceApiStub.TestJsonResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from TestJson operation
           */
            public void receiveErrorTestJson(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for SelectTagStatusList method
            * override this method for handling normal response from SelectTagStatusList operation
            */
           public void receiveResultSelectTagStatusList(
                    com.uradiosys.www.ServiceApiStub.SelectTagStatusListResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from SelectTagStatusList operation
           */
            public void receiveErrorSelectTagStatusList(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetTagStatus method
            * override this method for handling normal response from GetTagStatus operation
            */
           public void receiveResultGetTagStatus(
                    com.uradiosys.www.ServiceApiStub.GetTagStatusResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetTagStatus operation
           */
            public void receiveErrorGetTagStatus(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetConnectionString method
            * override this method for handling normal response from GetConnectionString operation
            */
           public void receiveResultGetConnectionString(
                    com.uradiosys.www.ServiceApiStub.GetConnectionStringResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetConnectionString operation
           */
            public void receiveErrorGetConnectionString(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for ActivateTagEvent method
            * override this method for handling normal response from ActivateTagEvent operation
            */
           public void receiveResultActivateTagEvent(
                    com.uradiosys.www.ServiceApiStub.ActivateTagEventResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from ActivateTagEvent operation
           */
            public void receiveErrorActivateTagEvent(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for ExchangeTagMac method
            * override this method for handling normal response from ExchangeTagMac operation
            */
           public void receiveResultExchangeTagMac(
                    com.uradiosys.www.ServiceApiStub.ExchangeTagMacResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from ExchangeTagMac operation
           */
            public void receiveErrorExchangeTagMac(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for SelectTagStatus method
            * override this method for handling normal response from SelectTagStatus operation
            */
           public void receiveResultSelectTagStatus(
                    com.uradiosys.www.ServiceApiStub.SelectTagStatusResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from SelectTagStatus operation
           */
            public void receiveErrorSelectTagStatus(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for SetWarningRuleCoverage method
            * override this method for handling normal response from SetWarningRuleCoverage operation
            */
           public void receiveResultSetWarningRuleCoverage(
                    com.uradiosys.www.ServiceApiStub.SetWarningRuleCoverageResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from SetWarningRuleCoverage operation
           */
            public void receiveErrorSetWarningRuleCoverage(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetLatestAlertInfo method
            * override this method for handling normal response from GetLatestAlertInfo operation
            */
           public void receiveResultGetLatestAlertInfo(
                    com.uradiosys.www.ServiceApiStub.GetLatestAlertInfoResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetLatestAlertInfo operation
           */
            public void receiveErrorGetLatestAlertInfo(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for InsertWarningRule method
            * override this method for handling normal response from InsertWarningRule operation
            */
           public void receiveResultInsertWarningRule(
                    com.uradiosys.www.ServiceApiStub.InsertWarningRuleResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from InsertWarningRule operation
           */
            public void receiveErrorInsertWarningRule(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for UpdateHostGroup method
            * override this method for handling normal response from UpdateHostGroup operation
            */
           public void receiveResultUpdateHostGroup(
                    com.uradiosys.www.ServiceApiStub.UpdateHostGroupResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from UpdateHostGroup operation
           */
            public void receiveErrorUpdateHostGroup(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetTagStatusListAndSummaryAllEvents method
            * override this method for handling normal response from GetTagStatusListAndSummaryAllEvents operation
            */
           public void receiveResultGetTagStatusListAndSummaryAllEvents(
                    com.uradiosys.www.ServiceApiStub.GetTagStatusListAndSummaryAllEventsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetTagStatusListAndSummaryAllEvents operation
           */
            public void receiveErrorGetTagStatusListAndSummaryAllEvents(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for DeleteWarningRuleCoverage method
            * override this method for handling normal response from DeleteWarningRuleCoverage operation
            */
           public void receiveResultDeleteWarningRuleCoverage(
                    com.uradiosys.www.ServiceApiStub.DeleteWarningRuleCoverageResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from DeleteWarningRuleCoverage operation
           */
            public void receiveErrorDeleteWarningRuleCoverage(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for ClearTagAllStatus method
            * override this method for handling normal response from ClearTagAllStatus operation
            */
           public void receiveResultClearTagAllStatus(
                    com.uradiosys.www.ServiceApiStub.ClearTagAllStatusResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from ClearTagAllStatus operation
           */
            public void receiveErrorClearTagAllStatus(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for SetSiteSurveyDataToServer method
            * override this method for handling normal response from SetSiteSurveyDataToServer operation
            */
           public void receiveResultSetSiteSurveyDataToServer(
                    com.uradiosys.www.ServiceApiStub.SetSiteSurveyDataToServerResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from SetSiteSurveyDataToServer operation
           */
            public void receiveErrorSetSiteSurveyDataToServer(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for HostTagAddOrUpdate method
            * override this method for handling normal response from HostTagAddOrUpdate operation
            */
           public void receiveResultHostTagAddOrUpdate(
                    com.uradiosys.www.ServiceApiStub.HostTagAddOrUpdateResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from HostTagAddOrUpdate operation
           */
            public void receiveErrorHostTagAddOrUpdate(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for SelectTagStatusListAndSummary method
            * override this method for handling normal response from SelectTagStatusListAndSummary operation
            */
           public void receiveResultSelectTagStatusListAndSummary(
                    com.uradiosys.www.ServiceApiStub.SelectTagStatusListAndSummaryResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from SelectTagStatusListAndSummary operation
           */
            public void receiveErrorSelectTagStatusListAndSummary(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for ChangePosition method
            * override this method for handling normal response from ChangePosition operation
            */
           public void receiveResultChangePosition(
                    com.uradiosys.www.ServiceApiStub.ChangePositionResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from ChangePosition operation
           */
            public void receiveErrorChangePosition(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetFacilityList method
            * override this method for handling normal response from GetFacilityList operation
            */
           public void receiveResultGetFacilityList(
                    com.uradiosys.www.ServiceApiStub.GetFacilityListResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetFacilityList operation
           */
            public void receiveErrorGetFacilityList(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for DeleteStatusByalertId method
            * override this method for handling normal response from DeleteStatusByalertId operation
            */
           public void receiveResultDeleteStatusByalertId(
                    com.uradiosys.www.ServiceApiStub.DeleteStatusByalertIdResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from DeleteStatusByalertId operation
           */
            public void receiveErrorDeleteStatusByalertId(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for DeleteWarningRule method
            * override this method for handling normal response from DeleteWarningRule operation
            */
           public void receiveResultDeleteWarningRule(
                    com.uradiosys.www.ServiceApiStub.DeleteWarningRuleResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from DeleteWarningRule operation
           */
            public void receiveErrorDeleteWarningRule(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetModelATagList method
            * override this method for handling normal response from GetModelATagList operation
            */
           public void receiveResultGetModelATagList(
                    com.uradiosys.www.ServiceApiStub.GetModelATagListResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetModelATagList operation
           */
            public void receiveErrorGetModelATagList(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetWarningRulesByAreaId method
            * override this method for handling normal response from GetWarningRulesByAreaId operation
            */
           public void receiveResultGetWarningRulesByAreaId(
                    com.uradiosys.www.ServiceApiStub.GetWarningRulesByAreaIdResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetWarningRulesByAreaId operation
           */
            public void receiveErrorGetWarningRulesByAreaId(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetMapAreaListByMapId method
            * override this method for handling normal response from GetMapAreaListByMapId operation
            */
           public void receiveResultGetMapAreaListByMapId(
                    com.uradiosys.www.ServiceApiStub.GetMapAreaListByMapIdResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetMapAreaListByMapId operation
           */
            public void receiveErrorGetMapAreaListByMapId(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetMapInfoByFacilityId method
            * override this method for handling normal response from GetMapInfoByFacilityId operation
            */
           public void receiveResultGetMapInfoByFacilityId(
                    com.uradiosys.www.ServiceApiStub.GetMapInfoByFacilityIdResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetMapInfoByFacilityId operation
           */
            public void receiveErrorGetMapInfoByFacilityId(java.lang.Exception e) {
            }
                


    }
    