import {Injectable} from '@angular/core';

@Injectable()
export class LocationService {

  locations = [
    {name: 'THREE_B_1', value: '3B-1'},
    {name: 'THREE_B_2', value: '3B-2'},
    {name: 'THREE_B_3', value: '3B-3'},
    {name: 'THREE_B_4', value: '3B-4'},
    {name: 'THREE_B_5', value: '3B-5'},
    {name: 'THREE_B_6', value: '3B-6'},
    {name: 'FOUR_B_1', value: '4B-1'},
    {name: 'FOUR_B_2', value: '4B-2'},
    {name: 'FOUR_B_3', value: '4B-3'},
    {name: 'FOUR_B_4', value: '4B-4'},
    {name: 'FOUR_B_5', value: '4B-5'},
    {name: 'FIVE_B_1', value: '5B-1'}
  ];

  constructor() {
  }

  getLocations() {
    return this.locations;
  }

  getLocationFromValue(value: string) {
    return this.locations.find(function (location) {
      return location.value === value;
    });
  }

  getLocationFromName(name: string) {
    return this.locations.find(function (location) {
      return location.name === name;
    });
  }

}
