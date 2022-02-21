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
// @ts-ignore: some imports may be unused
import { DeprecatedObject, DeprecatedObjectFromJSON, DeprecatedObjectFromJSONTyped, DeprecatedObjectToJSON } from './DeprecatedObject';

/**
 * 
 * @export
 * @interface ObjectWithDeprecatedFields
 */
export interface ObjectWithDeprecatedFields {
    /**
     * 
     * @type {string}
     * @memberof ObjectWithDeprecatedFields
     */
    uuid?: string;
    /**
     * 
     * @type {number}
     * @memberof ObjectWithDeprecatedFields
     * @deprecated
     */
    id?: number;
    /**
     * 
     * @type {DeprecatedObject}
     * @memberof ObjectWithDeprecatedFields
     * @deprecated
     */
    deprecatedRef?: DeprecatedObject;
    /**
     * 
     * @type {Array<string>}
     * @memberof ObjectWithDeprecatedFields
     * @deprecated
     */
    bars?: Array<string>;
}

export function ObjectWithDeprecatedFieldsFromJSON(json: any): ObjectWithDeprecatedFields {
    return ObjectWithDeprecatedFieldsFromJSONTyped(json, false);
}

// @ts-ignore: ignoreDiscriminator may be unused
export function ObjectWithDeprecatedFieldsFromJSONTyped(json: any, ignoreDiscriminator: boolean): ObjectWithDeprecatedFields {
    if ((json === undefined) || (json === null)) {
        return json;
    }
    return {
        
        'uuid': !exists(json, 'uuid') ? undefined : json['uuid'],
        'id': !exists(json, 'id') ? undefined : json['id'],
        'deprecatedRef': !exists(json, 'deprecatedRef') ? undefined : DeprecatedObjectFromJSON(json['deprecatedRef']),
        'bars': !exists(json, 'bars') ? undefined : json['bars'],
    };
}

export function ObjectWithDeprecatedFieldsToJSON(value?: ObjectWithDeprecatedFields | null): any {
    if (value === undefined) {
        return undefined;
    }
    if (value === null) {
        return null;
    }
    return {
        
        'uuid': value.uuid,
        'id': value.id,
        'deprecatedRef': DeprecatedObjectToJSON(value.deprecatedRef),
        'bars': value.bars,
    };
}

