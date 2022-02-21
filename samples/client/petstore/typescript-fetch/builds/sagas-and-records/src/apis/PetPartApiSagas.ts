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


// @ts-ignore: import may be unused
import {Api} from './';
// @ts-ignore: import may be unused
import {List} from 'immutable';
// @ts-ignore: some imports may be unused
import {all, fork, put, takeLatest} from "redux-saga/effects";
// @ts-ignore: some imports may be unused
import {apiCall, createSagaAction as originalCreateSagaAction, BaseEntitySupportPayloadApiAction, BasePayloadApiAction, NormalizedRecordEntities, normalizedEntities} from "../runtimeSagasAndRecords";
// @ts-ignore: import may be unused
import {Action} from "redux-ts-simple";

// @ts-ignore: some imports may be unused
import { GetMatchingPartsResponse, GetMatchingPartsResponseRecord, getMatchingPartsResponseRecordUtils, GetPetPartTypeResponse, GetPetPartTypeResponseRecord, getPetPartTypeResponseRecordUtils, MatchingPartsRecord, PetPartType } from '../models';

const createSagaAction = <T>(type: string) => originalCreateSagaAction<T>(type, {namespace: "api_petPartApi"});

export const petPartApiSagaMap = new Map<string, () => Generator<any, any, any>>([
        ["getFakePetPartType", getFakePetPartTypeSaga],
        ["getMatchingParts", getMatchingPartsSaga],
    ]
);

export function *petPartApiAllSagas() {
    yield all([...petPartApiSagaMap.values()].map(actionSaga => fork(actionSaga)));
}

//region getFakePetPartType

export interface PayloadGetFakePetPartType extends PayloadGetFakePetPartTypeRequest, BasePayloadApiAction {
}

export interface PayloadGetFakePetPartTypeRequest {
    fakePetPartId: string;
}

export const getFakePetPartTypeRequest = createSagaAction<PayloadGetFakePetPartTypeRequest>("getFakePetPartTypeRequest");
export const getFakePetPartTypeSuccess = createSagaAction<PetPartType>("getFakePetPartTypeSuccess");
export const getFakePetPartTypeFailure = createSagaAction<{error: any, requestPayload: PayloadGetFakePetPartType}>("getFakePetPartTypeFailure");

export const getFakePetPartType = createSagaAction<PayloadGetFakePetPartType>("getFakePetPartType");

export function *getFakePetPartTypeSaga() {
    yield takeLatest(getFakePetPartType, getFakePetPartTypeSagaImp);
}

export function *getFakePetPartTypeSagaImp(_action_: Action<PayloadGetFakePetPartType>) {
    const {markErrorsAsHandled, ..._payloadRest_} = _action_.payload;
    try {
        const {
            fakePetPartId,
        } = _payloadRest_;

        yield put(getFakePetPartTypeRequest(_action_.payload));

        const response: Required<GetPetPartTypeResponse> = yield apiCall(Api.petPartApi, Api.petPartApi.getFakePetPartType,
            parseFloat(fakePetPartId),
        );

        let successReturnValue: any = undefined;
            successReturnValue = getPetPartTypeResponseRecordUtils.fromApiPassthrough(response);
            yield put(getFakePetPartTypeSuccess(successReturnValue));

        return successReturnValue;
    } catch (error) {
        if (markErrorsAsHandled) {error.wasHandled = true; }
        yield put(getFakePetPartTypeFailure({error, requestPayload: _action_.payload}));
        return error;
    }
}
//endregion
//region getMatchingParts

export interface PayloadGetMatchingParts extends PayloadGetMatchingPartsRequest, BaseEntitySupportPayloadApiAction {
}

export interface PayloadGetMatchingPartsRequest {
    fakePetPartId: string;
    _long: boolean;
    smooth: boolean;
    _short: boolean;
    name?: string;
    connectedPart?: string;
}

export const getMatchingPartsRequest = createSagaAction<PayloadGetMatchingPartsRequest>("getMatchingPartsRequest");
export const getMatchingPartsSuccess = createSagaAction<MatchingPartsRecord>("getMatchingPartsSuccess");
export const getMatchingPartsSuccess_Entities = createSagaAction<NormalizedRecordEntities>("getMatchingPartsSuccess_Entities");
export const getMatchingPartsFailure = createSagaAction<{error: any, requestPayload: PayloadGetMatchingParts}>("getMatchingPartsFailure");

export const getMatchingParts = createSagaAction<PayloadGetMatchingParts>("getMatchingParts");

export function *getMatchingPartsSaga() {
    yield takeLatest(getMatchingParts, getMatchingPartsSagaImp);
}

export function *getMatchingPartsSagaImp(_action_: Action<PayloadGetMatchingParts>) {
    const {markErrorsAsHandled, ..._payloadRest_} = _action_.payload;
    try {
        const {toEntities, toInlined = !toEntities, ...requestPayload} = _payloadRest_;
        const {
            fakePetPartId,
            _long,
            smooth,
            _short,
            name,
            connectedPart,
        } = _payloadRest_;

        yield put(getMatchingPartsRequest(requestPayload));

        const response: Required<GetMatchingPartsResponse> = yield apiCall(Api.petPartApi, Api.petPartApi.getMatchingParts,
            parseFloat(fakePetPartId),
            _long,
            smooth,
            _short,
            name,
            connectedPart,
        );

        let successReturnValue: any = undefined;
        if (toEntities) {
            successReturnValue = getMatchingPartsResponseRecordUtils.fromApiPassthroughAsEntities(response);
            yield put(normalizedEntities(successReturnValue));
            yield put(getMatchingPartsSuccess_Entities(successReturnValue));
        }
        if (toInlined) {
            successReturnValue = getMatchingPartsResponseRecordUtils.fromApiPassthrough(response);
            yield put(getMatchingPartsSuccess(successReturnValue));
        }

        return successReturnValue;
    } catch (error) {
        if (markErrorsAsHandled) {error.wasHandled = true; }
        yield put(getMatchingPartsFailure({error, requestPayload: _action_.payload}));
        return error;
    }
}
//endregion
