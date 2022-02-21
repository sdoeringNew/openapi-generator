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

// @ts-ignore: some or all imports may be unused
import { exists, mapValues } from '../runtime';
// @ts-ignore: some imports may be unused
import { Pet, PetFromJSON, PetFromJSONTyped, PetToJSON } from './Pet';
// @ts-ignore: some imports may be unused
import { ResponseMeta, ResponseMetaFromJSON, ResponseMetaFromJSONTyped, ResponseMetaToJSON } from './ResponseMeta';

/**
 * 
 * @export
 * @interface FindPetsByStatusResponse
 */
export interface FindPetsByStatusResponse {
    /**
     * 
     * @type {ResponseMeta}
     * @memberof FindPetsByStatusResponse
     */
    meta: ResponseMeta;
    /**
     * 
     * @type {Array<Pet>}
     * @memberof FindPetsByStatusResponse
     */
    data?: Array<Pet>;
}

export function FindPetsByStatusResponseFromJSON(json: any): FindPetsByStatusResponse {
    return FindPetsByStatusResponseFromJSONTyped(json, false);
}

// @ts-ignore: ignoreDiscriminator may be unused
export function FindPetsByStatusResponseFromJSONTyped(json: any, ignoreDiscriminator: boolean): FindPetsByStatusResponse {
    if ((json === undefined) || (json === null)) {
        return json;
    }
    return {
        
        'meta': ResponseMetaFromJSON(json['meta']),
        'data': !exists(json, 'data') ? undefined : ((json['data'] as Array<any>).map(PetFromJSON)),
    };
}

export function FindPetsByStatusResponseToJSON(value?: FindPetsByStatusResponse | null): any {
    if (value === undefined) {
        return undefined;
    }
    if (value === null) {
        return null;
    }
    return {
        
        'meta': ResponseMetaToJSON(value.meta),
        'data': value.data === undefined ? undefined : ((value.data as Array<any>).map(PetToJSON)),
    };
}

