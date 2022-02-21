/* tslint:disable */
/* eslint-disable */
/**
 * OpenAPI Petstore
 * This is a sample server Petstore server. For this sample, you can use the api key `special-key` to test the authorization filters.
 *
 * The version of the OpenAPI document: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

// @ts-ignore: some imports may be unused
import {ApiRecordUtils, knownRecordFactories, appFromJS, NormalizedRecordEntities} from "../runtimeSagasAndRecords";
// @ts-ignore: import may be unused
import {getApiEntitiesState} from "../ApiEntitiesSelectors"
// @ts-ignore: some imports may be unused
import {List, Record, RecordOf, Map} from 'immutable';
import {Schema, schema, NormalizedSchema} from "normalizr";
// @ts-ignore: some imports may be unused
import {select, call} from "redux-saga/effects";

// @ts-ignore: some imports may be unused
import { GetBehaviorPermissionsResponse } from './GetBehaviorPermissionsResponse';

// @ts-ignore: some imports may be unused
import { ResponseMeta } from './ResponseMeta';

// @ts-ignore: some imports may be unused
import { ResponseMetaRecord, responseMetaRecordUtils } from './ResponseMetaRecord';

export const GetBehaviorPermissionsResponseRecordProps = {
    recType: "GetBehaviorPermissionsResponseApiRecord" as "GetBehaviorPermissionsResponseApiRecord",
    meta: ResponseMetaRecord(),
    data: null as { [key: string]: boolean; } | null,
};

export type GetBehaviorPermissionsResponseRecordPropsType = typeof GetBehaviorPermissionsResponseRecordProps;
export const GetBehaviorPermissionsResponseRecord = Record(GetBehaviorPermissionsResponseRecordProps, GetBehaviorPermissionsResponseRecordProps.recType);
export type GetBehaviorPermissionsResponseRecord = RecordOf<GetBehaviorPermissionsResponseRecordPropsType>;

knownRecordFactories.set(GetBehaviorPermissionsResponseRecordProps.recType, GetBehaviorPermissionsResponseRecord);


class GetBehaviorPermissionsResponseRecordUtils extends ApiRecordUtils<GetBehaviorPermissionsResponse, GetBehaviorPermissionsResponseRecord> {
    public normalize(apiObject: GetBehaviorPermissionsResponse, asEntity?: boolean): GetBehaviorPermissionsResponse {
        (apiObject as any).recType = GetBehaviorPermissionsResponseRecordProps.recType;
        responseMetaRecordUtils.normalize(apiObject.meta);
        return apiObject;
    }

    public toApi(record: GetBehaviorPermissionsResponseRecord): GetBehaviorPermissionsResponse {
        const apiObject = super.toApi(record);
        apiObject.meta = responseMetaRecordUtils.toApi(record.meta);
        return apiObject;
    }

    public fromApiPassthrough(apiObject: GetBehaviorPermissionsResponse): { [key: string]: boolean; } {
        return apiObject.data!;
    }

    public fromApiPassthroughAsEntities(apiObject: GetBehaviorPermissionsResponse): NormalizedRecordEntities {
        console.log("entities revival not supported on this response");
        return {entities: {}, result: List<string>()};
    }
}

export const getBehaviorPermissionsResponseRecordUtils = new GetBehaviorPermissionsResponseRecordUtils();


