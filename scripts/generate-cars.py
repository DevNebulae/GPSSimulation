import argparse
import json
import uuid

parser = argparse.ArgumentParser('car-generator')
parser.add_argument(
    '--amount', help='The amount of cars which will be generated for each country.', required=True, type=int)
parser.add_argument(
    '--countries', help='The countries the cars will be generated for.', nargs='+', required=True)

args = parser.parse_args()
amount = args.amount
countries = args.countries

cars = list()

for country in countries:
    for i in range(0, amount):
        cars.append({'id': str(uuid.uuid4()), 'country': country.upper()})

print(json.dumps(cars))
