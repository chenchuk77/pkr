#!/bin/bash

# this script copies the static folder (website) to remote server for fast deployment

ansible-playbook sync-web.yml -i hosts
