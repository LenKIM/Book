import requests 

URL = 'https://api.github.com/repos/LenKIM/Book/contents?ref=master' 
headers = {
    "Authorization": "token " + ${{ secrets.TOKEN }},
  }
response = requests.get(URL, headers=headers).json()
requestBody = responsei

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

readme_template = readme_template + "![header](https://capsule-render.vercel.app/api?type=wave&color=0:EEFF00,100:a82da8&height=300&section=footer&fontSize=90)"

f = open("README.md", 'w')
f.write(readme_template)
f.close()

import base64
abc = base64.b64encode(bytes(readme_template, 'utf-8'))

encoding_string = stringToBase64(readme_template);
URL = 'https://api.github.com/repos/LenKIM/Book/contents/README.md'
headers = {
    "Authorization": "token " + ${{ secrets.TOKEN }},
    "accept": "application/vnd.github.v3+json"
  }
data={'message': 'push by github actions', 
      'content': abc.decode('utf-8'),
      'sha': readme_sha,
      'commiter':{"name": "LenKIM",
                  "email":"joenggyu0@gmail.com"}
     }

response = requests.put(URL, headers=headers, json=data).json()
