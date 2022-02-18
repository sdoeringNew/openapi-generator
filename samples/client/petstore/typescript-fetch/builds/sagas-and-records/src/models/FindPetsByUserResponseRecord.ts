// @ts-nocheck
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

import {ApiRecordUtils, knownRecordFactories, appFromJS, NormalizedRecordEntities} from "../runtimeSagasAndRecords";
import {getApiEntitiesState} from "../ApiEntitiesSelectors"
import {List, Record, RecordOf, Map} from 'immutable';
import {Schema, schema, NormalizedSchema} from "normalizr";
import {select, call} from "redux-saga/effects";

import {
    FindPetsByUserResponse,
} from './FindPetsByUserResponse';

import {
    ResponseMeta,
} from './ResponseMeta';
import {
    User,
} from './User';

import {
    ResponseMetaRecord,
    responseMetaRecordUtils
} from './ResponseMetaRecord';
import {
    UserRecord,
    userRecordUtils
} from './UserRecord';

export const FindPetsByUserResponseRecordProps = {
    recType: "FindPetsByUserResponseApiRecord" as "FindPetsByUserResponseApiRecord",
    meta: ResponseMetaRecord(),
    data: (UserRecord(), null as List<UserRecord> | null),
};

export type FindPetsByUserResponseRecordPropsType = typeof FindPetsByUserResponseRecordProps;
export const FindPetsByUserResponseRecord = Record(FindPetsByUserResponseRecordProps, FindPetsByUserResponseRecordProps.recType);
export type FindPetsByUserResponseRecord = RecordOf<FindPetsByUserResponseRecordPropsType>;

knownRecordFactories.set(FindPetsByUserResponseRecordProps.recType, FindPetsByUserResponseRecord);


class FindPetsByUserResponseRecordUtils extends ApiRecordUtils<FindPetsByUserResponse, FindPetsByUserResponseRecord> {
    public normalize(apiObject: FindPetsByUserResponse, asEntity?: boolean): FindPetsByUserResponse {
        (apiObject as any).recType = FindPetsByUserResponseRecordProps.recType;
        responseMetaRecordUtils.normalize(apiObject.meta);
        if (apiObject.data) { userRecordUtils.normalizeArray(apiObject.data); } 
        return apiObject;
    }

    public toApi(record: FindPetsByUserResponseRecord): FindPetsByUserResponse {
        const apiObject = super.toApi(record);
        apiObject.meta = responseMetaRecordUtils.toApi(record.meta);
        if (record.data) { apiObject.data = userRecordUtils.toApiArray(record.data); } 
        return apiObject;
    }

    public fromApiPassthrough(apiObject: FindPetsByUserResponse): List<UserRecord> {
        if (!apiObject.data) {return List<UserRecord>(); }
        const normalizedApiObject = userRecordUtils.normalizeArray(apiObject.data);
        return appFromJS(normalizedApiObject);
    }

    public fromApiPassthroughAsEntities(apiObject: FindPetsByUserResponse): NormalizedRecordEntities {
        if (!apiObject.data) {return {entities: {}, result: List<string>()}; }
        return ApiRecordUtils.toNormalizedRecordEntities(userRecordUtils.normalizeArrayAsEntities(apiObject.data), true);
    }
}

export const findPetsByUserResponseRecordUtils = new FindPetsByUserResponseRecordUtils();


