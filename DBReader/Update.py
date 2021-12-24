import requests
import openpyxl
from pprint import pprint

tableName = "Students.xlsx"

url = "https://attendance-e88b.restdb.io/rest/attend"
api_key = "14d881bc8b38acfa4d7bbdb834c123302b05c"
headers = {'Accept': 'application/json', 'X-API-Key': api_key}

wb = openpyxl.load_workbook(tableName)
wb.active = 1
ls = wb.active

response = requests.get(url, headers=headers)

if response.status_code == 200:
    print('Connection success!')
    json_response = response.json()
    pprint(json_response)
    print(wb.active)

    for i in range(1, len(json_response) + 1):
        ls['A' + str(i + 1)] = (int(json_response[i - 1]['code']) // 10000) % 100
        ls['C' + str(i + 1)] = (int(json_response[i - 1]['code']) // 100) % 100
        ls['D' + str(i + 1)] = chr(int(json_response[i - 1]['code']) % 100)
        ls['E' + str(i + 1)] = json_response[i - 1]['date']
        ls['F' + str(i + 1)] = json_response[i - 1]['time']
else:
    print('Connection denied')
    print('Code: ', response.status_code)

wb.save(tableName)
