import requests
import sys
import base64

token = sys.argv

URL = 'https://api.github.com/repos/LenKIM/Book/contents?ref=master'
headers = {
    "Authorization": "token " + token[1],
}
response = requests.get(URL, headers=headers).json()
requestBody = response

item_collection = []
readme_sha = ""
for item in requestBody[1:]:
    if item["name"] == "README.md":
        readme_sha = item["sha"]
    abc = {
        "name": item["name"],
        "url": item["url"],
        "html_url": item["html_url"],
        "type": item["type"]
    }
    item_collection.append(abc)

for idx, item in enumerate(item_collection):
    item_sub_collection = []
    if item["type"] == 'dir':
        sub_files = requests.get(item["url"], headers=headers).json()
        for sub_file in sub_files:
            sub_file_item = {"sub_file_name": sub_file["name"],
                             "sub_file_url": sub_file["url"],
                             "sub_file_html_url": sub_file["html_url"],
                             "sub_file_type": sub_file["type"],
                             }
            item_sub_collection.append(sub_file_item)
    item["sub_files"] = item_sub_collection
    item_collection[idx] = item

readme_header_template = """

<div align="center">
    <h1> BookShelf </h1>
</div>

<p align="center">
    이 곳은 읽은 책을 보관하고, 필요시 책을 다시 살펴보기 위한 공간입니다.
<p align="center">
    <a href="https://www.notion.so/likelen/44c8b34f833541c2b45ebcdf00d39286">책 목록</a>
</p>
</p>

"""
abc = ""
for idx, item in enumerate(item_collection):
    name_string = str(idx) + '. ' + item["name"] + " [<a" + " href=" + "\"" + item[
        "html_url"] + "\"" + ">들여다보기 📂</a>]" + '\n\n'

    if item["type"] == "dir":
        sub_strings = "\n\n"
        sub_strings = sub_strings + """<details align="center">\n"""
        sub_strings = sub_strings + "<summary>" + "<h2><b>" + name_string + "</h2><b>"+"</summary>\n"
        sub_strings = sub_strings + "<br><p>\n"
        for idx, sub_item in enumerate(item["sub_files"]):
            if sub_item["sub_file_name"] == ".gitignore":
                continue
            if not sub_item["sub_file_name"].endswith('.md'):
                continue
            sub_name_string = "<h3> •️ " + sub_item["sub_file_name"] + " [<a" + " href=" + "\"" + sub_item["sub_file_html_url"] + "\"" + ">들여다보기 📂</a>]" + '\n\n\n'
            sub_strings = sub_strings + sub_name_string
        sub_strings = sub_strings + "</h3></p>\n"
        sub_strings = sub_strings + "</details>\n\n"
        abc = abc + sub_strings
    else:
        if not item["name"].endswith('.md'):
            continue

        # abc = abc + name_string + "\n\n"

readme_header_template = readme_header_template + abc

license = """
## License

Provided under the terms of the CC BY-NC 4.0 License.

Copyright © 2022, [Jeonggyu Kim](https://happy-coding-day.tistory.com/).
"""
readme_header_template = readme_header_template + license
f = open("README.md", 'w')
f.write(readme_header_template)
f.close()

abc = base64.b64encode(bytes(readme_header_template, 'utf-8'))
URL = 'https://api.github.com/repos/LenKIM/Book/contents/README.md'
headers = {
    "Authorization": "token " + token[1],
    "accept": "application/vnd.github.v3+json"
}
data = {'message': 'push by github actions',
        'content': abc.decode('utf-8'),
        'sha': readme_sha,
        'commiter': {"name": "LenKIM",
                     "email": "joenggyu0@gmail.com"}
        }

response = requests.put(URL, headers=headers, json=data).json()
