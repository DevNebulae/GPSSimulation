import argparse
import json

parser = argparse.ArgumentParser('tracker-filter')
parser.add_argument(
    '--country', help='The country you want to extract data for.', required=True)

args = parser.parse_args()
country = args.country

with open('../src/main/resources/trackers.json', 'r') as f:
    trackers = json.load(f)
    filteredTrackers = filter(
        lambda tracker: tracker['country'] == country, trackers)
    mappedTrackers = map(lambda tracker: tracker['id'], filteredTrackers)
    print(json.dumps(list(mappedTrackers)))
