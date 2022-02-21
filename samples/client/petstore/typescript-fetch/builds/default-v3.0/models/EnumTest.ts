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
import { OuterEnum, OuterEnumFromJSON, OuterEnumFromJSONTyped, OuterEnumToJSON } from './OuterEnum';
// @ts-ignore: some imports may be unused
import { OuterEnumDefaultValue, OuterEnumDefaultValueFromJSON, OuterEnumDefaultValueFromJSONTyped, OuterEnumDefaultValueToJSON } from './OuterEnumDefaultValue';
// @ts-ignore: some imports may be unused
import { OuterEnumInteger, OuterEnumIntegerFromJSON, OuterEnumIntegerFromJSONTyped, OuterEnumIntegerToJSON } from './OuterEnumInteger';
// @ts-ignore: some imports may be unused
import { OuterEnumIntegerDefaultValue, OuterEnumIntegerDefaultValueFromJSON, OuterEnumIntegerDefaultValueFromJSONTyped, OuterEnumIntegerDefaultValueToJSON } from './OuterEnumIntegerDefaultValue';

/**
 * 
 * @export
 * @interface EnumTest
 */
export interface EnumTest {
    /**
     * 
     * @type {string}
     * @memberof EnumTest
     */
    enumString?: EnumTestEnumStringEnum;
    /**
     * 
     * @type {string}
     * @memberof EnumTest
     */
    enumStringRequired: EnumTestEnumStringRequiredEnum;
    /**
     * 
     * @type {number}
     * @memberof EnumTest
     */
    enumInteger?: EnumTestEnumIntegerEnum;
    /**
     * 
     * @type {number}
     * @memberof EnumTest
     */
    enumNumber?: EnumTestEnumNumberEnum;
    /**
     * 
     * @type {OuterEnum}
     * @memberof EnumTest
     */
    outerEnum?: OuterEnum | null;
    /**
     * 
     * @type {OuterEnumInteger}
     * @memberof EnumTest
     */
    outerEnumInteger?: OuterEnumInteger;
    /**
     * 
     * @type {OuterEnumDefaultValue}
     * @memberof EnumTest
     */
    outerEnumDefaultValue?: OuterEnumDefaultValue;
    /**
     * 
     * @type {OuterEnumIntegerDefaultValue}
     * @memberof EnumTest
     */
    outerEnumIntegerDefaultValue?: OuterEnumIntegerDefaultValue;
}

/**
* @export
* @enum {string}
*/
export enum EnumTestEnumStringEnum {
    Upper = 'UPPER',
    Lower = 'lower',
    Empty = ''
}/**
* @export
* @enum {string}
*/
export enum EnumTestEnumStringRequiredEnum {
    Upper = 'UPPER',
    Lower = 'lower',
    Empty = ''
}/**
* @export
* @enum {string}
*/
export enum EnumTestEnumIntegerEnum {
    NUMBER_1 = 1,
    NUMBER_MINUS_1 = -1
}/**
* @export
* @enum {string}
*/
export enum EnumTestEnumNumberEnum {
    NUMBER_1_DOT_1 = 1.1,
    NUMBER_MINUS_1_DOT_2 = -1.2
}

export function EnumTestFromJSON(json: any): EnumTest {
    return EnumTestFromJSONTyped(json, false);
}

// @ts-ignore: ignoreDiscriminator may be unused
export function EnumTestFromJSONTyped(json: any, ignoreDiscriminator: boolean): EnumTest {
    if ((json === undefined) || (json === null)) {
        return json;
    }
    return {
        
        'enumString': !exists(json, 'enum_string') ? undefined : json['enum_string'],
        'enumStringRequired': json['enum_string_required'],
        'enumInteger': !exists(json, 'enum_integer') ? undefined : json['enum_integer'],
        'enumNumber': !exists(json, 'enum_number') ? undefined : json['enum_number'],
        'outerEnum': !exists(json, 'outerEnum') ? undefined : OuterEnumFromJSON(json['outerEnum']),
        'outerEnumInteger': !exists(json, 'outerEnumInteger') ? undefined : OuterEnumIntegerFromJSON(json['outerEnumInteger']),
        'outerEnumDefaultValue': !exists(json, 'outerEnumDefaultValue') ? undefined : OuterEnumDefaultValueFromJSON(json['outerEnumDefaultValue']),
        'outerEnumIntegerDefaultValue': !exists(json, 'outerEnumIntegerDefaultValue') ? undefined : OuterEnumIntegerDefaultValueFromJSON(json['outerEnumIntegerDefaultValue']),
    };
}

export function EnumTestToJSON(value?: EnumTest | null): any {
    if (value === undefined) {
        return undefined;
    }
    if (value === null) {
        return null;
    }
    return {
        
        'enum_string': value.enumString,
        'enum_string_required': value.enumStringRequired,
        'enum_integer': value.enumInteger,
        'enum_number': value.enumNumber,
        'outerEnum': OuterEnumToJSON(value.outerEnum),
        'outerEnumInteger': OuterEnumIntegerToJSON(value.outerEnumInteger),
        'outerEnumDefaultValue': OuterEnumDefaultValueToJSON(value.outerEnumDefaultValue),
        'outerEnumIntegerDefaultValue': OuterEnumIntegerDefaultValueToJSON(value.outerEnumIntegerDefaultValue),
    };
}

