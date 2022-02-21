/* tslint:disable */
/* eslint-disable */
/**
 * OpenAPI Petstore
 * This spec is mainly for testing Petstore server and contains fake endpoints, models. Please do not use this for any other purpose. Special characters: \" \\
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
/**
 * 
 * @export
 * @interface List
 */
export interface List {
    /**
     * 
     * @type {string}
     * @memberof List
     */
    _123list?: string;
}

export function ListFromJSON(json: any): List {
    return ListFromJSONTyped(json, false);
}

// @ts-ignore: ignoreDiscriminator may be unused
export function ListFromJSONTyped(json: any, ignoreDiscriminator: boolean): List {
    if ((json === undefined) || (json === null)) {
        return json;
    }
    return {
        
        '_123list': !exists(json, '123-list') ? undefined : json['123-list'],
    };
}

export function ListToJSON(value?: List | null): any {
    if (value === undefined) {
        return undefined;
    }
    if (value === null) {
        return null;
    }
    return {
        
        '123-list': value._123list,
    };
}

