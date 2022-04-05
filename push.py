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
           "type":item["type"]
          }
    item_collection.append(abc)

for idx, item in enumerate(item_collection):
    item_sub_collection = []
    if item["type"] == 'dir':
        sub_files = requests.get(item["url"], headers=headers).json()
        for sub_file in sub_files:
            sub_file_item = { "sub_file_name": sub_file["name"],
                             "sub_file_url": sub_file["url"],
                             "sub_file_html_url": sub_file["html_url"],
                             "sub_file_type": sub_file["type"],
                           }
            item_sub_collection.append(sub_file_item)
    item["sub_files"] = item_sub_collection
    item_collection[idx] = item

readme_template = """


![header](https://capsule-render.vercel.app/api?type=wave&color=0:EEFF00,100:a82da8&height=300&section=header&text=Reading_Book&fontSize=90)
<div align="center">
  <h1> 책장 </h1>
</div>

<p align="center">
  이 곳은 읽을 책을 보관하고, 필요시 책을 다시 살펴보기 위한 공간입니다.
</p>
[독서 목록](https://www.notion.so/likelen/44c8b34f833541c2b45ebcdf00d39286)


"""
abc = ""
for idx, item in enumerate(item_collection):
    name_string = str(idx)+'. ' + item["name"] + " [<a"+ " href="+ "\"" +item["html_url"]+"\""+">링크</a>]</h2>" + '\n\n'
    if item["type"] == "dir":
        sub_strings = "\n\n"
        sub_strings = sub_strings + "<details>\n"
        sub_strings = sub_strings + "<summary>"+ name_string + "</summary>\n"
        sub_strings = sub_strings + "<p>\n"
        for idx, sub_item in enumerate(item["sub_files"]):
            sub_name_string = "&emsp;&emsp;▪️ " + sub_item["sub_file_name"] + " [<a"+ " href="+ "\"" +sub_item["sub_file_html_url"]+"\""+">링크</a>]" + '\n\n\n'
            sub_strings = sub_strings + sub_name_string
        sub_strings = sub_strings + "</p>\n"
        sub_strings = sub_strings + "</details>\n\n"
        abc = abc + sub_strings
    else:
        abc = abc + name_string + "\n\n"

readme_template = readme_template + abc

license = """
## License

Provided under the terms of the CC BY-NC 4.0 License.

Copyright © 2022, [Jeonggyu Kim](https://www.posquit0.com).
"""
readme_template = readme_template + license
f = open("README.md", 'w')
f.write(readme_template)
f.close()


abc = base64.b64encode(bytes(readme_template, 'utf-8'))
URL = 'https://api.github.com/repos/LenKIM/Book/contents/README.md'
headers = {
    "Authorization": "token " + token[1],
    "accept": "application/vnd.github.v3+json"
  }
data={'message': 'push by github actions',
      'content': abc.decode('utf-8'),
      'sha': readme_sha,
      'commiter':{"name": "LenKIM",
                  "email":"joenggyu0@gmail.com"}
     }

response = requests.put(URL, headers=headers, json=data).json()
