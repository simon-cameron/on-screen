package graphql_client

import (
	"net/http"
	"io/ioutil"
	"bytes"
	"encoding/json"
	"log"
	"os"
	"fmt"
)

type Config struct {
	BaseImageUrl string `json:"image_base_url"`
}

func GetBaseImageUrl() string {
	endpoint := fmt.Sprintf("%s%s", os.Getenv("CONFIGURATION_ENDPOINT"), "/v1/api/configuration")
	fmt.Println(endpoint)
	resp, err := http.Get(endpoint)
	
	if err != nil {
		log.Fatalln(err)
	}

	defer resp.Body.Close()

	body, err := ioutil.ReadAll(resp.Body)

	if err != nil {
		log.Fatalln(err)
	}

	config := Config{}

	errs := json.Unmarshal(body, &config)

	if errs != nil {
		log.Fatalln(err)
	}

	return config.BaseImageUrl
}

func Query(query string, obj interface{}) {//interface{} {
	requestBody, err := json.Marshal(map[string]string{
		"query": query,
	})

	if err != nil {
		log.Fatalln(err)
	}

	resp, err := http.Post(fmt.Sprintf("%s%s", os.Getenv("GRAPHQL_ENDPOINT"),"/graphql"), "application/json", bytes.NewBuffer(requestBody))
	
	if err != nil {
		log.Fatalln(err)
	}

	defer resp.Body.Close()

	body, err := ioutil.ReadAll(resp.Body)

	if err != nil {
		log.Fatalln(err)
	}

	errs := json.Unmarshal(body, &obj)

	if errs != nil {
		log.Fatalln(err)
	}
}

