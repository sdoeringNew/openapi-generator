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

/**
 * 
 * @export
 * @enum {string}
 */
export enum OuterEnumIntegerDefaultValue {
    NUMBER_0 = 0,
    NUMBER_1 = 1,
    NUMBER_2 = 2
}

export function OuterEnumIntegerDefaultValueFromJSON(json: any): OuterEnumIntegerDefaultValue {
    return OuterEnumIntegerDefaultValueFromJSONTyped(json, false);
}

// @ts-ignore: ignoreDiscriminator not used
export function OuterEnumIntegerDefaultValueFromJSONTyped(json: any, ignoreDiscriminator: boolean): OuterEnumIntegerDefaultValue {
    return json as OuterEnumIntegerDefaultValue;
}

export function OuterEnumIntegerDefaultValueToJSON(value?: OuterEnumIntegerDefaultValue | null): any {
    return value as any;
}

